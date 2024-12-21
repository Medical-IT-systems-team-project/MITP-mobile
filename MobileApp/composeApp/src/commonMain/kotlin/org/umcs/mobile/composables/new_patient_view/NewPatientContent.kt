package org.umcs.mobile.composables.new_patient_view

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.slapps.cupertino.adaptive.AdaptiveCheckbox
import com.slapps.cupertino.adaptive.ExperimentalAdaptiveApi
import com.slapps.cupertino.adaptive.Theme
import com.slapps.cupertino.adaptive.icons.AdaptiveIcons
import com.slapps.cupertino.adaptive.icons.DateRange
import com.slapps.cupertino.theme.CupertinoTheme
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.umcs.mobile.composables.shared.AdaptiveWheelDatePicker
import org.umcs.mobile.composables.shared.AppTextField
import org.umcs.mobile.data.Gender
import org.umcs.mobile.data.Patient
import org.umcs.mobile.theme.determineTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPatientContent(
    modifier : Modifier = Modifier,
    paddingValues: PaddingValues,
    showDatePicker: Boolean,
    datePickerState: SheetState,
    onShowDatePickerChange: (Boolean) -> Unit,
    onNewPatientChange: (Patient) -> Unit,
    newPatient: Patient,
    focusRequester: FocusRequester,
) {
    var shownDateOfBirth by remember { mutableStateOf("") }
    var ssnError by remember { mutableStateOf("") }
    var firstNameError by remember { mutableStateOf("") }
    var lastNameError by remember { mutableStateOf("") }
    var ageError by remember { mutableStateOf("") }
    var genderError by remember { mutableStateOf("") }
    var addressError by remember { mutableStateOf("") }
    var phoneNumberError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var dateOfBirthError by remember { mutableStateOf("") }
    val ssnMaxChars = 11
    val maxPhoneNumberChars = 9
    val theme = remember { determineTheme() }
    val isCupertino = when (theme) {
        Theme.Cupertino -> true
        Theme.Material3 -> false
    }
    val buttonTextStyle =
        if (isCupertino) CupertinoTheme.typography.title3 else MaterialTheme.typography.titleMedium.copy(
            fontSize = 20.sp
        )
    val scope = rememberCoroutineScope()
    val isFormValid = newPatient.socialSecurityNumber.isNotBlank() &&
            newPatient.firstName.isNotBlank() &&
            newPatient.lastName.isNotBlank() &&
            newPatient.age.isNotBlank() &&
            newPatient.gender.isNotBlank() &&
            newPatient.address.isNotBlank() &&
            newPatient.phoneNumber.isNotBlank() &&
            newPatient.email.isNotBlank() &&
            newPatient.birthDate.isNotBlank()

    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize().padding(horizontal = 25.dp).padding(
            top = paddingValues.calculateTopPadding() + 20.dp,
            bottom = paddingValues.calculateBottomPadding()
        )
    ) {
        if (showDatePicker) {
            AdaptiveWheelDatePicker(
                sheetState = datePickerState,
                dismiss = { newDate: LocalDate ->
                    onShowDatePickerChange(false)
                    onNewPatientChange(newPatient.copy(birthDate = newDate.toString()))
                    shownDateOfBirth = newDate.toString().replace('-', '/')
                }
            )
        }
        GenderSelection(
            selectedGender = newPatient.gender,
            onGenderSelected = { onNewPatientChange(newPatient.copy(gender = it)) },
        )
        AppTextField(
            title = { Text("First Name") },
            text = newPatient.firstName,
            supportingText = firstNameError,
            onTextChange = { onNewPatientChange(newPatient.copy(firstName = it)) },
            focusRequester = focusRequester,
            placeholder = { Text("First Name") },
        )
        AppTextField(
            title = { Text("Last Name") },
            text = newPatient.lastName,
            supportingText = lastNameError,
            onTextChange = { onNewPatientChange(newPatient.copy(lastName = it)) },
            focusRequester = focusRequester,
            placeholder = { Text("Last Name") },
        )
        AppTextField(
            keyboardType = KeyboardType.Number,
            title = { Text("Social Security Number") },
            text = newPatient.socialSecurityNumber,
            supportingText = ssnError,
            onTextChange = {
                if (it.length <= ssnMaxChars) {
                    onNewPatientChange(newPatient.copy(socialSecurityNumber = it))
                }
            },
            focusRequester = focusRequester,
            placeholder = { Text("Social Security Number") },
        )
        AppTextField(
            readOnly = true,
            keyboardType = KeyboardType.Number,
            title = { Text("Date of Birth") },
            text = shownDateOfBirth,
            supportingText = dateOfBirthError,
            focusRequester = focusRequester,
            placeholder = { Text("Date of Birth") },
            trailingIcon = {
                Icon(AdaptiveIcons.Outlined.DateRange, contentDescription = "Select date of birth")
            },
            onTextChange = { onNewPatientChange(newPatient.copy(birthDate = it)) },
            modifier = Modifier
                .pointerInput(newPatient.birthDate) {
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
            title = { Text("Age") },
            text = newPatient.age,
            supportingText = ageError,
            onTextChange = { onNewPatientChange(newPatient.copy(age = it)) },
            focusRequester = focusRequester,
            placeholder = { Text("Age") },
        )
        AppTextField(
            title = { Text("Address") },
            text = newPatient.address,
            supportingText = addressError,
            onTextChange = { onNewPatientChange(newPatient.copy(address = it)) },
            focusRequester = focusRequester,
            placeholder = { Text("Address") },
        )
        AppTextField(
            keyboardType = KeyboardType.Phone,
            title = { Text("Phone Number") },
            text = newPatient.phoneNumber,
            supportingText = phoneNumberError,
            onTextChange = {
                if (it.length <= maxPhoneNumberChars) {
                    onNewPatientChange(newPatient.copy(phoneNumber = it))
                }
            },
            focusRequester = focusRequester,
            placeholder = { Text("Phone Number") }
        )
        AppTextField(
            keyboardType = KeyboardType.Email,
            title = { Text("Email") },
            text = newPatient.email,
            supportingText = emailError,
            onTextChange = { onNewPatientChange(newPatient.copy(email = it)) },
            focusRequester = focusRequester,
            placeholder = { Text("Email") },
        )

        Spacer(modifier = Modifier.height(30.dp))
        Button(
            onClick = {
                ssnError = ""
                firstNameError = ""
                lastNameError = ""
                ageError = ""
                genderError = ""
                addressError = ""
                phoneNumberError = ""
                emailError = ""
                dateOfBirthError = ""

                if (isFormValid) {
                    scope.launch {
                        //   GlobalKtorClient.createNewPatient(newPatient)
                    }
                } else {
                    newPatient.socialSecurityNumber.ifBlank {
                        ssnError = "This field can't be blank"
                    }
                    newPatient.firstName.ifBlank {
                        firstNameError = "This field can't be blank"
                    }
                    newPatient.lastName.ifBlank {
                        lastNameError = "This field can't be blank"
                    }
                    newPatient.age.ifBlank {
                        ageError = "This field can't be blank"
                    }
                    newPatient.gender.ifBlank {
                        genderError = "This field can't be blank"
                    }
                    newPatient.address.ifBlank {
                        addressError = "This field can't be blank"
                    }
                    newPatient.phoneNumber.ifBlank {
                        phoneNumberError = "This field can't be blank"
                    }
                    newPatient.email.ifBlank {
                        emailError = "This field can't be blank"
                    }
                    newPatient.birthDate.ifBlank {
                        dateOfBirthError = "This field can't be blank"
                    }
                }

                Logger.i(newPatient.toString(), tag = "Patient")
            },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.size(width = 270.dp, height = 60.dp),
        ) {
            Text(text = "Create Patient", style = buttonTextStyle)
        }
    }
}

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun GenderSelection(
    selectedGender: String,
    onGenderSelected: (String) -> Unit,
) {
    val genderOptions = listOf(Gender.MALE.name, Gender.FEMALE.name)
    val theme = remember { determineTheme() }
    val isCupertino = when (theme) {
        Theme.Cupertino -> true
        Theme.Material3 -> false
    }
    val titleStyle = if (isCupertino) CupertinoTheme.typography.title3.copy(
        fontSize = 20.sp
    ) else MaterialTheme.typography.bodyLarge
    val textStyle =
        if (isCupertino) CupertinoTheme.typography.body else MaterialTheme.typography.bodyMedium
    val width = if(isCupertino) 300.dp else 270.dp

    Column {
        Text("Gender", style = titleStyle)
        Row(
            modifier = Modifier.width(width),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            genderOptions.forEach { gender ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    AdaptiveCheckbox(
                        checked = selectedGender == gender,
                        onCheckedChange = { onGenderSelected(gender) },
                    )
                    Text(
                        text = gender,
                        style = textStyle,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}