package org.umcs.mobile.composables.new_case_view

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.slapps.cupertino.adaptive.icons.Face
import com.slapps.cupertino.theme.CupertinoTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import org.umcs.mobile.composables.shared.AdaptiveTextField
import org.umcs.mobile.composables.shared.AdaptiveWheelDateTimePicker
import org.umcs.mobile.composables.shared.PatientPicker
import org.umcs.mobile.network.GlobalKtorClient
import org.umcs.mobile.theme.determineTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewCaseContent(
    //TODO: add navigating back after success
    paddingValues: PaddingValues,
    showPatientPicker: Boolean,
    showDatePicker: Boolean,
    patientPickerState: SheetState,
    newCase: MedicalCase,
    focusRequester: FocusRequester,
    focusManager: FocusManager,
    onNewCaseChange: (MedicalCase) -> Unit,
    onShowPatientPickerChange: (Boolean) -> Unit,
    onShowDatePickerChange: (Boolean) -> Unit,
    doctorID: String,
) {
    val theme = remember { determineTheme() }
    val isCupertino = when (theme) {
        Theme.Cupertino -> true
        Theme.Material3 -> false
    }
    val verticalSpacing = if (isCupertino) 15.dp else 5.dp
    val buttonTextStyle =
        if (isCupertino) CupertinoTheme.typography.title3 else MaterialTheme.typography.titleMedium.copy(
            fontSize = 20.sp
        )
    var shownDate by remember { mutableStateOf("") }
    var patientError by remember { mutableStateOf("") }
    var dateError by remember { mutableStateOf("") }
    var reasonError by remember { mutableStateOf("") }
    var descriptionError by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val isFormValid = newCase.getPatientFullName().isNotBlank() &&
            newCase.admissionDate.isNotBlank() &&
            newCase.admissionReason.isNotBlank() &&
            newCase.description.isNotBlank()

    Column(
        verticalArrangement = Arrangement.spacedBy(verticalSpacing),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(horizontal = 25.dp).padding(
            top = paddingValues.calculateTopPadding() + 20.dp
        )
    ) {
        if (showPatientPicker) {
            PatientPicker(
                pickPatient = { onNewCaseChange(newCase.copy(patient = it)) },
                patientPickerState = patientPickerState,
                onDismiss = {
                    onShowPatientPickerChange(false)
                    focusManager.clearFocus()
                },
            )
        }
        if (showDatePicker) {
            AdaptiveWheelDateTimePicker(
                sheetState = rememberModalBottomSheetState(),
                dismiss = { newDateTime: LocalDateTime ->
                    onShowDatePickerChange(false)
                    onNewCaseChange(newCase.copy(admissionDate = newDateTime.toString()))
                    shownDate = newDateTime.toString().replace('-', '/').replace('T', ' ')
                }
            )
            /*   DatePickerModal(
                   onDateSelected = { dateInMillis ->
                       if (dateInMillis != null) {
                           val newDate = convertMillisToDate(dateInMillis)
                           onNewCaseChange(newCase.copy(admissionDate = newDate))
                       }
                   },
                   onDismiss = {
                       onShowDatePickerChange(false)
                       focusManager.clearFocus()
                   }
               )*/
        }
        AdaptiveTextField(
            readOnly = true,
            title = { Text("Patient ") },
            text = newCase.getPatientFullName(),
            supportingText = patientError,
            changeSupportingText = { patientError = it },
            onTextChange = { },
            focusRequester = focusRequester,
            placeholder = { Text("Patient") },
            trailingIcon = {
                Icon(AdaptiveIcons.Outlined.Face, contentDescription = "Select Patient")
            },
            modifier = Modifier
                .pointerInput(newCase.getPatientFullName()) {
                    awaitEachGesture {
                        awaitFirstDown(pass = PointerEventPass.Initial)
                        val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                        if (upEvent != null) {
                            onShowPatientPickerChange(true)
                        }
                    }
                }
        )
        AdaptiveTextField(
            readOnly = true,
            keyboardType = KeyboardType.Number,
            title = { Text("Admission date") },
            text = shownDate,
            supportingText = dateError,
            changeSupportingText = { dateError = it },
            focusRequester = focusRequester,
            placeholder = { Text("Admission date") },
            trailingIcon = {
                Icon(AdaptiveIcons.Outlined.DateRange, contentDescription = "Select date")
            },
            onTextChange = { onNewCaseChange(newCase.copy(admissionDate = it)) },
            modifier = Modifier
                .pointerInput(newCase.admissionDate) {
                    awaitEachGesture {
                        awaitFirstDown(pass = PointerEventPass.Initial)
                        val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                        if (upEvent != null) {
                            onShowDatePickerChange(true)
                        }
                    }
                }
        )
        AdaptiveTextField(
            title = { Text("Admission Reason") },
            text = newCase.admissionReason,
            supportingText = reasonError,
            changeSupportingText = { reasonError = it },
            onTextChange = { onNewCaseChange(newCase.copy(admissionReason = it)) },
            focusRequester = focusRequester,
            placeholder = { Text("Admission Reason") },
        )
        AdaptiveTextField(
            maxLines = 5,
            isSingleLine = false,
            title = { Text("Description") },
            text = newCase.description,
            supportingText = descriptionError,
            changeSupportingText = { descriptionError = it },
            onTextChange = { onNewCaseChange(newCase.copy(description = it)) },
            focusRequester = focusRequester,
            placeholder = { Text("Description") },
        )

        Spacer(modifier = Modifier.height(30.dp)) //fixme

        AdaptiveTonalButton(
            onClick = {
                handleCreateCase(
                    newCase = newCase,
                    doctorID = doctorID,
                    scope = scope,
                    isFormValid = isFormValid,
                    changePatientError = { patientError = it },
                    changeDateError = { dateError = it },
                    changeReasonError = { reasonError = it },
                    changeDescriptionError = { descriptionError = it }
                )
            },
            modifier = Modifier.then(
                if (isCupertino) Modifier.fillMaxWidth() else Modifier.size(
                    width = 270.dp,
                    height = 60.dp
                )
            ),
            adaptation = {
                cupertino {
                    colors = CupertinoButtonDefaults.filledButtonColors(
                        contentColor = CupertinoTheme.colorScheme.label
                    )
                }
            }
        ) {
            Text(text = "Create Case", fontSize = 16.sp)
        }
    }
}

private fun handleCreateCase(
    newCase: MedicalCase,
    doctorID: String,
    scope: CoroutineScope,
    isFormValid: Boolean,
    changePatientError: (String) -> Unit,
    changeDateError: (String) -> Unit,
    changeReasonError: (String) -> Unit,
    changeDescriptionError: (String) -> Unit,
) {
    changePatientError("")
    changeDateError("")
    changeReasonError("")
    changeDescriptionError("")

    if (isFormValid) {
        scope.launch {
            GlobalKtorClient.createNewCase(newCase, doctorID)
        }
    } else {
        newCase.getPatientFullName().ifBlank {
            changePatientError("This field can't be blank")
        }
        newCase.admissionDate.ifBlank {
            changeDateError("This field can't be blank")
        }
        newCase.admissionReason.ifBlank {
            changeReasonError("This field can't be blank")
        }
        newCase.description.ifBlank {
            changeDescriptionError("This field can't be blank")
        }
    }

    Logger.i(newCase.toString(), tag = "Case")
}