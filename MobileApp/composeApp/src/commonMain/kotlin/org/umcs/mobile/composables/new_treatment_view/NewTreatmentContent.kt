package org.umcs.mobile.composables.new_treatment_view

import AppViewModel
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.touchlab.kermit.Logger
import com.slapps.cupertino.CupertinoButtonDefaults
import com.slapps.cupertino.adaptive.AdaptiveTonalButton
import com.slapps.cupertino.adaptive.Theme
import com.slapps.cupertino.adaptive.icons.AdaptiveIcons
import com.slapps.cupertino.adaptive.icons.DateRange
import com.slapps.cupertino.theme.CupertinoTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel
import org.umcs.mobile.composables.shared.AdaptiveTextField
import org.umcs.mobile.composables.shared.AdaptiveWheelDateTimePicker
import org.umcs.mobile.network.AllMedicalCasesResult
import org.umcs.mobile.network.AllPatientsResult
import org.umcs.mobile.network.CreateNewTreatmentResult
import org.umcs.mobile.network.GlobalKtorClient
import org.umcs.mobile.network.dto.case.MedicalCaseResponseDto
import org.umcs.mobile.network.dto.patient.PatientResponseDto
import org.umcs.mobile.theme.determineTheme
import org.umcs.mobile.theme.onSurfaceDark
import kotlin.reflect.KFunction1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTreatmentContent(
    paddingValues: PaddingValues,
    showStartDatePicker: Boolean,
    showEndDatePicker: Boolean,
    startDatePickerState: SheetState,
    endDatePickerState: SheetState,
    onShowStartDatePickerChange: (Boolean) -> Unit,
    onShowEndDatePickerChange: (Boolean) -> Unit,
    onNewTreatmentChange: (MedicalTreatment) -> Unit,
    newTreatment: MedicalTreatment,
    focusRequester: FocusRequester,
    focusManager: FocusManager,
    medicalCaseID: Int,
    doctorID: String,
    modifier: Modifier,
    navigateBack: () -> Unit,
    viewmodel: AppViewModel = koinViewModel(),
) {
    val theme = remember { determineTheme() }
    val isCupertino = when (theme) {
        Theme.Cupertino -> true
        Theme.Material3 -> false
    }
    val buttonTextStyle =
        if (isCupertino) CupertinoTheme.typography.title3 else MaterialTheme.typography.titleMedium.copy(
            fontSize = 20.sp
        )
    val verticalSpacing = if (isCupertino) 15.dp else 5.dp
    var shownStartDate by remember { mutableStateOf("") }
    var shownEndDate by remember { mutableStateOf("") }
    var descriptionError by remember { mutableStateOf("") }
    var startDateError by remember { mutableStateOf("") }
    var endDateError by remember { mutableStateOf("") }
    var detailsError by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val isFormValid = newTreatment.description.isNotBlank() &&
            newTreatment.startDate.isNotBlank() &&
            newTreatment.endDate.isNotBlank() &&
            newTreatment.name.isNotBlank() &&
            newTreatment.details.isNotBlank()

    Column(
        verticalArrangement = Arrangement.spacedBy(verticalSpacing),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize().padding(horizontal = 25.dp).padding(
            top = paddingValues.calculateTopPadding() + 20.dp
        )
    ) {
        if (showStartDatePicker) {
            AdaptiveWheelDateTimePicker(
                sheetState = startDatePickerState,
                dismiss = { newDateTime: LocalDateTime ->
                    onShowStartDatePickerChange(false)
                    onNewTreatmentChange(newTreatment.copy(startDate = newDateTime.toString()))
                    shownStartDate = newDateTime.toString().replace('-', '/').replace('T', ' ')
                }
            )
        }
        if (showEndDatePicker) {
            AdaptiveWheelDateTimePicker(
                sheetState = endDatePickerState,
                dismiss = { newDateTime: LocalDateTime ->
                    onShowEndDatePickerChange(false)
                    onNewTreatmentChange(newTreatment.copy(endDate = newDateTime.toString()))
                    shownEndDate = newDateTime.toString().replace('-', '/').replace('T', ' ')
                },
                passedStartDateTime =
                if (newTreatment.startDate.isNotBlank()) {
                    val timeZone = TimeZone.currentSystemDefault()
                    val startDate = LocalDateTime.parse(newTreatment.startDate)
                    val instant = startDate.toInstant(timeZone)
                    val instantOneDayLater = instant.plus(1,DateTimeUnit.DAY,timeZone)
                    val localDateTimeOneDayLater = instantOneDayLater.toLocalDateTime(timeZone)
                    localDateTimeOneDayLater
                } else {
                    null
                }
            )
        }
        AdaptiveTextField(
            readOnly = true,
            keyboardType = KeyboardType.Number,
            title = { Text("Start Date") },
            text = shownStartDate,
            supportingText = startDateError,
            changeSupportingText = { startDateError = it },
            focusRequester = focusRequester,
            placeholder = { Text("Start Date") },
            trailingIcon = {
                Icon(AdaptiveIcons.Outlined.DateRange, contentDescription = "Select start date")
            },
            onTextChange = { onNewTreatmentChange(newTreatment.copy(startDate = it)) },
            modifier = Modifier
                .pointerInput(newTreatment.startDate) {
                    awaitEachGesture {
                        awaitFirstDown(pass = PointerEventPass.Initial)
                        val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                        if (upEvent != null) {
                            onShowStartDatePickerChange(true)
                        }
                    }
                }
        )
        AdaptiveTextField(
            readOnly = true,
            keyboardType = KeyboardType.Number,
            title = { Text("End Date") },
            text = shownEndDate,
            supportingText = endDateError,
            changeSupportingText = { endDateError = it },
            focusRequester = focusRequester,
            placeholder = { Text("End Date") },
            trailingIcon = {
                Icon(AdaptiveIcons.Outlined.DateRange, contentDescription = "Select end date")
            },
            onTextChange = { onNewTreatmentChange(newTreatment.copy(endDate = it)) },
            modifier = Modifier
                .pointerInput(newTreatment.endDate) {
                    awaitEachGesture {
                        awaitFirstDown(pass = PointerEventPass.Initial)
                        val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                        if (upEvent != null) {
                            onShowEndDatePickerChange(true)
                        }
                    }
                }
        )
        AdaptiveTextField(
            title = { Text("Name") },
            text = newTreatment.name,
            supportingText = nameError,
            changeSupportingText = { nameError = it },
            onTextChange = { onNewTreatmentChange(newTreatment.copy(name = it)) },
            focusRequester = focusRequester,
            placeholder = { Text("Name") },
        )
        AdaptiveTextField(
            title = { Text("Description") },
            text = newTreatment.description,
            supportingText = descriptionError,
            changeSupportingText = { descriptionError = it },
            onTextChange = { onNewTreatmentChange(newTreatment.copy(description = it)) },
            focusRequester = focusRequester,
            placeholder = { Text("Description") },
        )
        AdaptiveTextField(
            title = { Text("Details") },
            text = newTreatment.details,
            supportingText = detailsError,
            changeSupportingText = { detailsError = it },
            onTextChange = { onNewTreatmentChange(newTreatment.copy(details = it)) },
            focusRequester = focusRequester,
            placeholder = { Text("Details") },
        )

        Spacer(modifier = Modifier.height(30.dp))

        AdaptiveTonalButton(
            onClick = {
                handleCreateTreatment(
                    newTreatment = newTreatment,
                    doctorID = doctorID,
                    medicalCaseID = medicalCaseID,
                    scope = scope,
                    isFormValid = isFormValid,
                    changeDescriptionError = { descriptionError = it },
                    changeStartDateError = { startDateError = it },
                    changeEndDateError = { endDateError = it },
                    changeDetailsError = { detailsError = it },
                    changeNameError = { nameError = it },
                    navigateBack = navigateBack,
                    updateCases = viewmodel::setMedicalCases,
                    updatePatients = viewmodel::setPatients
                )
            },
            modifier = Modifier.then(
                if (isCupertino) Modifier.fillMaxWidth() else Modifier.size(
                    width = 270.dp,
                    height = 60.dp
                )
            ),
            adaptation = {
                material {
                    colors = ButtonDefaults.filledTonalButtonColors(
                        contentColor = onSurfaceDark,
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                }
                cupertino {
                    colors = CupertinoButtonDefaults.filledButtonColors(
                        contentColor = onSurfaceDark,
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                }
            }
        ) {
            Text(text = "Create Treatment", fontSize = 16.sp)
        }
    }
}

private fun handleCreateTreatment(
    newTreatment: MedicalTreatment,
    doctorID: String,
    medicalCaseID: Int,
    scope: CoroutineScope,
    isFormValid: Boolean,
    changeDescriptionError: (String) -> Unit,
    changeStartDateError: (String) -> Unit,
    changeEndDateError: (String) -> Unit,
    changeDetailsError: (String) -> Unit,
    changeNameError: (String) -> Unit,
    updatePatients: KFunction1<List<PatientResponseDto>, Unit>,
    updateCases: KFunction1<List<MedicalCaseResponseDto>, Unit>,
    navigateBack: () -> Unit,
) {
    changeDescriptionError("")
    changeStartDateError("")
    changeEndDateError("")
    changeDetailsError("")
    changeNameError("")

    if (isFormValid) {
        scope.launch {
            val createNewTreatmentResult =
                GlobalKtorClient.createNewTreatment(newTreatment, doctorID, medicalCaseID)

            when (createNewTreatmentResult) {
                is CreateNewTreatmentResult.Error -> Logger.i(createNewTreatmentResult.message)
                CreateNewTreatmentResult.Success -> {
                    val patientsResult = GlobalKtorClient.getAllDoctorPatients()
                    val casesResult = GlobalKtorClient.getAllMedicalCasesAsDoctor()

                    if (patientsResult is AllPatientsResult.Success) {
                        updatePatients(patientsResult.patients)
                    }
                    if (casesResult is AllMedicalCasesResult.Success) {
                        updateCases(casesResult.cases)
                    }
                    navigateBack()
                }
            }
        }
    } else {
        newTreatment.description.ifBlank {
            changeDescriptionError("This field can't be blank")
        }
        newTreatment.startDate.ifBlank {
            changeStartDateError("This field can't be blank")
        }
        newTreatment.endDate.ifBlank {
            changeEndDateError("This field can't be blank")
        }
        newTreatment.details.ifBlank {
            changeDetailsError("This field can't be blank")
        }
        newTreatment.name.ifBlank {
            changeNameError("This field can't be blank")
        }
    }

    Logger.i(newTreatment.toString(), tag = "Treatment")
}

