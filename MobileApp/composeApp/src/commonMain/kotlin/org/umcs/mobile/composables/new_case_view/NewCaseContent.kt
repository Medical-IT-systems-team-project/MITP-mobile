package org.umcs.mobile.composables.new_case_view

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
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.slapps.cupertino.adaptive.Theme
import com.slapps.cupertino.adaptive.icons.AdaptiveIcons
import com.slapps.cupertino.adaptive.icons.DateRange
import com.slapps.cupertino.adaptive.icons.Face
import com.slapps.cupertino.theme.CupertinoTheme
import kotlinx.datetime.LocalDateTime
import org.umcs.mobile.composables.shared.AdaptiveWheelDateTimePicker
import org.umcs.mobile.composables.shared.AppTextField
import org.umcs.mobile.composables.shared.PatientPicker
import org.umcs.mobile.theme.determineTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewCaseContent(
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
) {
    val theme = remember{determineTheme()}
    val isCupertino = when(theme){
        Theme.Cupertino -> true
        Theme.Material3 -> false
    }
    val buttonTextStyle = if(isCupertino) CupertinoTheme.typography.title3 else MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp)

    var patientError by remember{mutableStateOf("")}
    var dateError by remember{mutableStateOf("")}
    var reasonError by remember{mutableStateOf("")}
    var descriptionError by remember{mutableStateOf("")}

    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
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
                dismiss = { newDateTime : LocalDateTime ->
                    onShowDatePickerChange(false)
                    val newDate = newDateTime.toString().replace('-','/').replace('T',' ')
                    onNewCaseChange(newCase.copy(admissionDate =  newDate))
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
        AppTextField(
            readOnly = true,
            title = { Text("Patient ") },
            text = newCase.getPatientFullName(),
            supportingText = patientError,
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
        AppTextField(
            readOnly = true,
            keyboardType = KeyboardType.Number,
            title = { Text("Admission date") },
            text = newCase.admissionDate,
            supportingText = dateError,
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
        AppTextField(
            title = { Text("Admission Reason") },
            text = newCase.details,
            supportingText = reasonError,
            onTextChange = { onNewCaseChange(newCase.copy(details = it)) },
            focusRequester = focusRequester,
            placeholder = { Text("Admission Reason") },
        )
        AppTextField(
            maxLines = 5,
            isSingleLine = false,
            title = { Text("Description") },
            text = newCase.diagnosis,
            supportingText = descriptionError,
            onTextChange = { onNewCaseChange(newCase.copy(diagnosis = it)) },
            focusRequester = focusRequester,
            placeholder = { Text("Description") },
        )

        Spacer(modifier = Modifier.height(30.dp))
        Button(
            onClick = {
                patientError = ""
                dateError = ""
                reasonError = ""
                descriptionError = ""

                newCase.getPatientFullName().ifBlank {
                    patientError = "This field can't be blank"
                }
                newCase.admissionDate.ifBlank {
                    dateError = "This field can't be blank"
                }
                newCase.details.ifBlank {
                    reasonError = "This field can't be blank"
                }
                newCase.diagnosis.ifBlank {
                    descriptionError = "This field can't be blank"
                }

                Logger.i(newCase.toString(),tag ="Case")
            },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.size(width = 270.dp, height = 60.dp),
        ) {
            Text(text = "Create Case", style = buttonTextStyle)
        }
    }
}
