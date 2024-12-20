package org.umcs.mobile.composables.new_medication_view

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import com.slapps.cupertino.adaptive.Theme
import com.slapps.cupertino.adaptive.icons.AdaptiveIcons
import com.slapps.cupertino.adaptive.icons.DateRange
import com.slapps.cupertino.theme.CupertinoTheme
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.umcs.mobile.composables.shared.AdaptiveWheelDatePicker
import org.umcs.mobile.composables.shared.AppTextField
import org.umcs.mobile.network.GlobalKtorClient
import org.umcs.mobile.theme.determineTheme

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
    doctorID: Int,
    modifier: Modifier,
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

    var shownStartDate by remember { mutableStateOf("") }
    var shownEndDate by remember { mutableStateOf("") }

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
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize().padding(horizontal = 25.dp).padding(
            top = paddingValues.calculateTopPadding() + 20.dp
        )
    ) {
        if (showStartDatePicker) {
            AdaptiveWheelDatePicker(
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
                sheetState = endDatePickerState,
                dismiss = { newDateTime: LocalDate ->
                    onShowEndDatePickerChange(false)
                    onNewMedicationChange(newMedication.copy(endDate = newDateTime.toString()))
                    shownEndDate = newDateTime.toString().replace('-', '/')
                },
                passedStartDate = if (newMedication.startDate.isNotBlank()) LocalDate.parse(newMedication.startDate) else null
            )
        }
        AppTextField(
            readOnly = true,
            keyboardType = KeyboardType.Number,
            title = { Text("Start Date") },
            text = shownStartDate,
            supportingText = startDateError,
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
        AppTextField(
            readOnly = true,
            keyboardType = KeyboardType.Number,
            title = { Text("End Date") },
            text = shownEndDate,
            supportingText = endDateError,
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
        AppTextField(
            title = { Text("Name") },
            text = newMedication.name,
            supportingText = nameError,
            onTextChange = { onNewMedicationChange(newMedication.copy(name = it)) },
            focusRequester = focusRequester,
            placeholder = { Text("Name") },
        )
        AppTextField(
            title = { Text("Details") },
            text = newMedication.details,
            supportingText = detailsError,
            onTextChange = { onNewMedicationChange(newMedication.copy(details = it)) },
            focusRequester = focusRequester,
            placeholder = { Text("Details") },
        )
        AppTextField(
            title = { Text("Dosage Form") },
            text = newMedication.dosageForm,
            supportingText = dosageError,
            onTextChange = { onNewMedicationChange(newMedication.copy(dosageForm = it)) },
            focusRequester = focusRequester,
            placeholder = { Text("Dosage Form") },
        )
        AppTextField(
            title = { Text("Strength") },
            text = newMedication.strength,
            supportingText = strengthError,
            onTextChange = { onNewMedicationChange(newMedication.copy(strength = it)) },
            focusRequester = focusRequester,
            placeholder = { Text("Strength") },
        )
        AppTextField(
            title = { Text("Unit") },
            text = newMedication.unit,
            supportingText = unitError,
            onTextChange = { onNewMedicationChange(newMedication.copy(unit = it)) },
            focusRequester = focusRequester,
            placeholder = { Text("Unit") },
        )

        Spacer(modifier = Modifier.height(30.dp))
        Button(
            onClick = {
                strengthError = ""
                startDateError = ""
                endDateError = ""
                detailsError = ""
                nameError = ""
                unitError =""
                dosageError =""

                if (isFormValid) {
                    scope.launch {
                        GlobalKtorClient.createNewMedication(newMedication, doctorID, medicalCaseID)
                    }
                } else {
                    newMedication.name.ifBlank {
                        nameError = "This field can't be blank"
                    }
                    newMedication.startDate.ifBlank {
                        startDateError = "This field can't be blank"
                    }
                    newMedication.endDate.ifBlank {
                        endDateError = "This field can't be blank"
                    }
                    newMedication.details.ifBlank {
                        detailsError = "This field can't be blank"
                    }
                    newMedication.dosageForm.ifBlank {
                        dosageError = "This field can't be blank"
                    }
                    newMedication.strength.ifBlank {
                        strengthError = "This field can't be blank"
                    }
                    newMedication.unit.ifBlank {
                        unitError = "This field can't be blank"
                    }
                }

                Logger.i(newMedication.toString(), tag = "Medication")
            },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.size(width = 270.dp, height = 60.dp),
        ) {
            Text(text = "Create Medication", style = buttonTextStyle)
        }
    }
}
