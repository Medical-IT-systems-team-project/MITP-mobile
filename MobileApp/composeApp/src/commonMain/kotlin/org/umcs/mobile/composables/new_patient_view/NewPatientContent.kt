package org.umcs.mobile.composables.new_patient_view

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import org.umcs.mobile.composables.shared.AppTextField
import org.umcs.mobile.composables.shared.DatePickerModal
import org.umcs.mobile.data.Patient
import org.umcs.mobile.data.convertMillisToDate

@Composable
fun NewPatientContent(
    newPatient: Patient,
    paddingValues: PaddingValues,
    formState: PatientFormState,
    focusRequester: FocusRequester,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onGenderChange: (String) -> Unit,
    onAgeChange: (String) -> Unit,
    onSocialSecurityNumberChange: (String) -> Unit,
    onDateOfBirthChange: (String) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(horizontal = 25.dp).padding(
            top = paddingValues.calculateTopPadding() + 20.dp
        )
    ) {
        if (showDatePicker) {
            DatePickerModal(
                onDateSelected = { dateInMillis ->
                    if (dateInMillis !=null) {
                        val newDate = convertMillisToDate(dateInMillis)
                        onDateOfBirthChange(newDate)
                    }
                },
                onDismiss = { showDatePicker = false }
            )
        }
        AppTextField(
            title = { Text("First Name") },
            text = newPatient.firstName,
            supportingText = formState.firstNameError,
            focusRequester = focusRequester,
            onTextChange = onFirstNameChange,
        )
        AppTextField(
            title = { Text("Last Name") },
            text = newPatient.lastName,
            supportingText = formState.lastNameError,
            focusRequester = focusRequester,
            onTextChange = onLastNameChange,
        )
        AppTextField(
            title = { Text("Gender") },
            text = newPatient.gender,
            supportingText = formState.genderError,
            focusRequester = focusRequester,
            onTextChange = onGenderChange,
        )
        AppTextField(
            keyboardType = KeyboardType.Number,
            title = { Text("Age") },
            text = newPatient.age,
            supportingText = formState.ageError,
            focusRequester = focusRequester,
            onTextChange = onAgeChange,
        )
        AppTextField(
            keyboardType = KeyboardType.Number,
            title = { Text("Social Security Number") },
            text = newPatient.socialSecurityNumber,
            supportingText = formState.socialSecurityNumberError,
            focusRequester = focusRequester,
            onTextChange = onSocialSecurityNumberChange,
        )
        AppTextField(
            readOnly = true,
            keyboardType = KeyboardType.Number,
            title = { Text("Date of Birth") },
            text = newPatient.dateOfBirth,
            supportingText = formState.dateOfBirthError,
            focusRequester = focusRequester,
            placeholder = { Text("DD/MM/YYYY") },
            trailingIcon = {
                Icon(Icons.Default.DateRange, contentDescription = "Select date")
            },
            onTextChange = onDateOfBirthChange,
            modifier = Modifier
                .pointerInput(newPatient.dateOfBirth) {
                    awaitEachGesture {
                        awaitFirstDown(pass = PointerEventPass.Initial)
                        val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                        if (upEvent != null) {
                            showDatePicker = true
                        }
                    }
                }
        )

        Spacer(modifier = Modifier.height(30.dp))
        Button(
            onClick = {
                Logger.i(newPatient.toString(),tag ="Patient")
            },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.size(width = 270.dp, height = 60.dp),
        ) {
            Text(text = "Create Patient", fontSize = 16.sp)
        }
    }
}
