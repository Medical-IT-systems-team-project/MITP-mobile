package org.umcs.mobile.composables.new_medication_view

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
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel
import org.umcs.mobile.composables.shared.AdaptiveTextField
import org.umcs.mobile.composables.shared.AdaptiveWheelDatePicker
import org.umcs.mobile.network.AllMedicalCasesResult
import org.umcs.mobile.network.AllPatientsResult
import org.umcs.mobile.network.CreateNewMedicationResult
import org.umcs.mobile.network.GlobalKtorClient
import org.umcs.mobile.network.dto.case.MedicalCaseResponseDto
import org.umcs.mobile.network.dto.patient.PatientResponseDto
import org.umcs.mobile.theme.determineTheme
import org.umcs.mobile.theme.onSurfaceDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewMedicationContent(
    paddingValues: PaddingValues,
    showStartDatePicker: Boolean,
    showEndDatePicker: Boolean,
    startDatePickerState: SheetState,
    endDatePickerState: SheetState,
    onShowStartDatePickerChange: (Boolean) -> Unit,
    onShowEndDatePickerChange: (Boolean) -> Unit,
    onNewMedicationChange: (Medication) -> Unit,
    newMedication: Medication,
    focusRequester: FocusRequester,
    medicalCaseID: Int,
    doctorID: String,
    modifier: Modifier,
    navigateBack: () -> Unit,
    viewmodel : AppViewModel = koinViewModel()
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

    var frequencyError by remember { mutableStateOf("") }
    var dosageError by remember { mutableStateOf("") }
    var unitError by remember { mutableStateOf("") }
    var strengthError by remember { mutableStateOf("") }
    var startDateError by remember { mutableStateOf("") }
    var endDateError by remember { mutableStateOf("") }
    var detailsError by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val isFormValid = newMedication.dosageForm.isNotBlank() &&
            newMedication.startDate.isNotBlank() &&
            newMedication.endDate.isNotBlank() &&
            newMedication.name.isNotBlank() &&
            newMedication.details.isNotBlank() &&
            newMedication.strength.isNotBlank() &&
            newMedication.unit.isNotBlank()

    Column(
        verticalArrangement = Arrangement.spacedBy(verticalSpacing),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize().padding(horizontal = 25.dp).padding(
            top = paddingValues.calculateTopPadding() + 20.dp
        )
    ) {
        if (showStartDatePicker) {
            AdaptiveWheelDatePicker(
                maxDate =  LocalDate(
                    year = 2030,
                    monthNumber = 1,
                    dayOfMonth = 1,
                ),
                sheetState = startDatePickerState,
                dismiss = { newDateTime: LocalDate ->
                    onShowStartDatePickerChange(false)
                    onNewMedicationChange(newMedication.copy(startDate = newDateTime.toString()))
                    shownStartDate = newDateTime.toString().replace('-', '/')
                }
            )
        }
        if (showEndDatePicker) {
            AdaptiveWheelDatePicker(
                maxDate =  LocalDate(
                    year = 2030,
                    monthNumber = 1,
                    dayOfMonth = 1,
                ),
                sheetState = endDatePickerState,
                dismiss = { newDateTime: LocalDate ->
                    onShowEndDatePickerChange(false)
                    onNewMedicationChange(newMedication.copy(endDate = newDateTime.toString()))
                    shownEndDate = newDateTime.toString().replace('-', '/')
                },
                passedStartDate = if (newMedication.startDate.isNotBlank()) {
                    val startDate = LocalDate.parse(newMedication.startDate)
                    startDate.plus(1,DateTimeUnit.DAY)
                }else {
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
            onTextChange = { onNewMedicationChange(newMedication.copy(startDate = it)) },
            modifier = Modifier
                .pointerInput(newMedication.startDate) {
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
            onTextChange = { onNewMedicationChange(newMedication.copy(endDate = it)) },
            modifier = Modifier
                .pointerInput(newMedication.endDate) {
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
            text = newMedication.name,
            supportingText = nameError,
            changeSupportingText = { nameError = it },
            onTextChange = { onNewMedicationChange(newMedication.copy(name = it)) },
            focusRequester = focusRequester,
            placeholder = { Text("Name") },
        )
        AdaptiveTextField(
            title = { Text("Details") },
            text = newMedication.details,
            supportingText = detailsError,
            changeSupportingText = { detailsError = it },
            onTextChange = { onNewMedicationChange(newMedication.copy(details = it)) },
            focusRequester = focusRequester,
            placeholder = { Text("Details") },
        )
        AdaptiveTextField(
            title = { Text("Dosage Form") },
            text = newMedication.dosageForm,
            supportingText = dosageError,
            changeSupportingText = { dosageError = it },
            onTextChange = { onNewMedicationChange(newMedication.copy(dosageForm = it)) },
            focusRequester = focusRequester,
            placeholder = { Text("Dosage Form") },
        )
        AdaptiveTextField(
            title = { Text("Frequency") },
            text = newMedication.frequency,
            supportingText = frequencyError,
            changeSupportingText = { frequencyError = it },
            onTextChange = { onNewMedicationChange(newMedication.copy(frequency = it)) },
            focusRequester = focusRequester,
            placeholder = { Text("Frequency") },
        )
        AdaptiveTextField(
            title = { Text("Strength") },
            text = newMedication.strength,
            supportingText = strengthError,
            changeSupportingText = { strengthError = it },
            onTextChange = { onNewMedicationChange(newMedication.copy(strength = it)) },
            focusRequester = focusRequester,
            placeholder = { Text("Strength") },
        )
        AdaptiveTextField(
            title = { Text("Unit") },
            text = newMedication.unit,
            supportingText = unitError,
            changeSupportingText = { unitError = it },
            onTextChange = { onNewMedicationChange(newMedication.copy(unit = it)) },
            focusRequester = focusRequester,
            placeholder = { Text("Unit") },
        )

        Spacer(modifier = Modifier.height(30.dp))

        AdaptiveTonalButton(
            onClick = {
                handleCreateMedication(
                    newMedication = newMedication,
                    doctorID = doctorID,
                    medicalCaseID = medicalCaseID,
                    scope = scope,
                    isFormValid = isFormValid,
                    changeFrequencyError = { frequencyError = it },
                    changeDosageError = { dosageError = it },
                    changeUnitError = { unitError = it },
                    changeStrengthError = { strengthError = it },
                    changeStartDateError = { startDateError = it },
                    changeEndDateError = { endDateError = it },
                    changeDetailsError = { detailsError = it },
                    changeNameError = { nameError = it },
                    navigateBack = navigateBack,
                    updatePatients = viewmodel::setPatients,
                    updateCases = viewmodel::setMedicalCases
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
            Text(text = "Create Medication", fontSize = 16.sp)
        }

    }
}

private fun handleCreateMedication(
    navigateBack: () -> Unit,
    updatePatients: (List<PatientResponseDto>) -> Unit,
    updateCases: (List<MedicalCaseResponseDto>) -> Unit,
    newMedication: Medication,
    doctorID: String,
    medicalCaseID: Int,
    scope: CoroutineScope,
    isFormValid: Boolean,
    changeDosageError: (String) -> Unit,
    changeUnitError: (String) -> Unit,
    changeStrengthError: (String) -> Unit,
    changeStartDateError: (String) -> Unit,
    changeEndDateError: (String) -> Unit,
    changeDetailsError: (String) -> Unit,
    changeNameError: (String) -> Unit,
    changeFrequencyError: (String) -> Unit,
) {
    blankMedicationErrors(
        changeDosageError,
        changeUnitError,
        changeStrengthError,
        changeStartDateError,
        changeEndDateError,
        changeDetailsError,
        changeNameError,
        changeFrequencyError
    )

    if (isFormValid) {
        scope.launch {
            val createNewMedicationResult =
                GlobalKtorClient.createNewMedication(newMedication, doctorID, medicalCaseID)

            when (createNewMedicationResult) {
                is CreateNewMedicationResult.Error -> Logger.i(createNewMedicationResult.message)
                CreateNewMedicationResult.Success -> {
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
        newMedication.name.ifBlank {
            changeNameError("This field can't be blank")
        }
        newMedication.startDate.ifBlank {
            changeStartDateError("This field can't be blank")
        }
        newMedication.endDate.ifBlank {
            changeEndDateError("This field can't be blank")
        }
        newMedication.details.ifBlank {
            changeDetailsError("This field can't be blank")
        }
        newMedication.dosageForm.ifBlank {
            changeDosageError("This field can't be blank")
        }
        newMedication.strength.ifBlank {
            changeStrengthError("This field can't be blank")
        }
        newMedication.unit.ifBlank {
            changeUnitError("This field can't be blank")
        }
        newMedication.frequency.ifBlank {
            changeFrequencyError("This field can't be blank")
        }
    }

    Logger.i(newMedication.toString(), tag = "Medication")
}

private fun blankMedicationErrors(
    changeDosageError: (String) -> Unit,
    changeUnitError: (String) -> Unit,
    changeStrengthError: (String) -> Unit,
    changeStartDateError: (String) -> Unit,
    changeEndDateError: (String) -> Unit,
    changeDetailsError: (String) -> Unit,
    changeNameError: (String) -> Unit,
    changeFrequencyError: (String) -> Unit,
) {
    changeDosageError("")
    changeUnitError("")
    changeStrengthError("")
    changeStartDateError("")
    changeEndDateError("")
    changeDetailsError("")
    changeNameError("")
    changeFrequencyError("")
}

@Serializable
data class MedicationFormErrors  (
    val idError : String?,
    val nameError: String?,
    val startDateError : String?,
    val endDateError : String?,
    val dosageError : String?,
    val frequencyError : String?,
    val strengthError : String?,
    val unitError : String?,
    val medicalDoctorNameError : String?,
    val statusError : String?
)