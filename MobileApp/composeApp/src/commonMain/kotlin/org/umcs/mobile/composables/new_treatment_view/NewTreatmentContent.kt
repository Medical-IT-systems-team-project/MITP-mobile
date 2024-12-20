package org.umcs.mobile.composables.new_treatment_view

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
import com.slapps.cupertino.theme.CupertinoTheme
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import org.umcs.mobile.composables.shared.AdaptiveWheelDateTimePicker
import org.umcs.mobile.composables.shared.AppTextField
import org.umcs.mobile.network.GlobalKtorClient
import org.umcs.mobile.theme.determineTheme

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
        verticalArrangement = Arrangement.spacedBy(5.dp),
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
                passedStartDateTime = if (newTreatment.startDate.isNotBlank()) LocalDateTime.parse(newTreatment.startDate) else null
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
        AppTextField(
            title = { Text("Name") },
            text = newTreatment.name,
            supportingText = nameError,
            onTextChange = { onNewTreatmentChange(newTreatment.copy(name = it)) },
            focusRequester = focusRequester,
            placeholder = { Text("Name") },
        )
        AppTextField(
            title = { Text("Description") },
            text = newTreatment.description,
            supportingText = descriptionError,
            onTextChange = { onNewTreatmentChange(newTreatment.copy(description = it)) },
            focusRequester = focusRequester,
            placeholder = { Text("Description") },
        )
        AppTextField(
            title = { Text("Details") },
            text = newTreatment.details,
            supportingText = detailsError,
            onTextChange = { onNewTreatmentChange(newTreatment.copy(details = it)) },
            focusRequester = focusRequester,
            placeholder = { Text("Details") },
        )

        Spacer(modifier = Modifier.height(30.dp))
        Button(
            onClick = {
                descriptionError = ""
                startDateError = ""
                endDateError = ""
                detailsError = ""
                nameError = ""

                if (isFormValid) {
                    scope.launch {
                        GlobalKtorClient.createNewTreatment(newTreatment, doctorID, medicalCaseID)
                    }
                } else {
                    newTreatment.description.ifBlank {
                        descriptionError = "This field can't be blank"
                    }
                    newTreatment.startDate.ifBlank {
                        startDateError = "This field can't be blank"
                    }
                    newTreatment.endDate.ifBlank {
                        endDateError = "This field can't be blank"
                    }
                    newTreatment.details.ifBlank {
                        detailsError = "This field can't be blank"
                    }
                    newTreatment.name.ifBlank {
                        nameError = "This field can't be blank"
                    }
                }

                Logger.i(newTreatment.toString(), tag = "Treatment")
            },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.size(width = 270.dp, height = 60.dp),
        ) {
            Text(text = "Create Treatment", style = buttonTextStyle)
        }
    }
}
