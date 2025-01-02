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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.slapps.cupertino.CupertinoCheckboxDefaults
import com.slapps.cupertino.adaptive.AdaptiveCheckbox
import com.slapps.cupertino.adaptive.AdaptiveTonalButton
import com.slapps.cupertino.adaptive.ExperimentalAdaptiveApi
import com.slapps.cupertino.adaptive.Theme
import com.slapps.cupertino.adaptive.icons.AdaptiveIcons
import com.slapps.cupertino.adaptive.icons.DateRange
import com.slapps.cupertino.theme.CupertinoTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.umcs.mobile.composables.shared.AdaptiveTextField
import org.umcs.mobile.composables.shared.AdaptiveWheelDatePicker
import org.umcs.mobile.data.Gender
import org.umcs.mobile.data.Patient
import org.umcs.mobile.network.CreatePatientResult
import org.umcs.mobile.network.GlobalKtorClient
import org.umcs.mobile.theme.cupertinoGray
import org.umcs.mobile.theme.cupertinoGrayInactive
import org.umcs.mobile.theme.determineTheme
import org.umcs.mobile.theme.onSurfaceDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPatientContent(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
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
    var formError by remember { mutableStateOf("") }
    val ssnMaxChars = 11
    val maxPhoneNumberChars = 9
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
        verticalArrangement = Arrangement.spacedBy(verticalSpacing),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize().padding(horizontal = 25.dp).padding(
            top = paddingValues.calculateTopPadding() + 20.dp,
            bottom = paddingValues.calculateBottomPadding()
        )
    ) {
        if (showDatePicker) {
            AdaptiveWheelDatePicker(
                minimumDate = LocalDate(
                    year = 1900,
                    monthNumber = 1,
                    dayOfMonth = 1,
                ),
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
        AdaptiveTextField(
            title = { Text("First Name") },
            text = newPatient.firstName,
            supportingText = firstNameError,
            changeSupportingText = { firstNameError = it },
            onTextChange = { onNewPatientChange(newPatient.copy(firstName = it)) },
            focusRequester = focusRequester,
            placeholder = { Text("First Name") },
        )
        AdaptiveTextField(
            title = { Text("Last Name") },
            text = newPatient.lastName,
            supportingText = lastNameError,
            changeSupportingText = { lastNameError = it },
            onTextChange = { onNewPatientChange(newPatient.copy(lastName = it)) },
            focusRequester = focusRequester,
            placeholder = { Text("Last Name") },
        )
        AdaptiveTextField(
            keyboardType = KeyboardType.Number,
            title = { Text("Social Security Number") },
            text = newPatient.socialSecurityNumber,
            supportingText = ssnError,
            changeSupportingText = { ssnError = it },
            onTextChange = {
                if (it.length <= ssnMaxChars) {
                    onNewPatientChange(newPatient.copy(socialSecurityNumber = it))
                }
            },
            focusRequester = focusRequester,
            placeholder = { Text("Social Security Number") },
        )
        AdaptiveTextField(
            readOnly = true,
            keyboardType = KeyboardType.Number,
            title = { Text("Date of Birth") },
            text = shownDateOfBirth,
            supportingText = dateOfBirthError,
            changeSupportingText = { dateOfBirthError = it },
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
        AdaptiveTextField(
            title = { Text("Age") },
            text = newPatient.age,
            supportingText = ageError,
            changeSupportingText = { ageError = it },
            onTextChange = { onNewPatientChange(newPatient.copy(age = it)) },
            focusRequester = focusRequester,
            placeholder = { Text("Age") },
        )
        AdaptiveTextField(
            title = { Text("Address") },
            text = newPatient.address,
            supportingText = addressError,
            changeSupportingText = { addressError = it },
            onTextChange = { onNewPatientChange(newPatient.copy(address = it)) },
            focusRequester = focusRequester,
            placeholder = { Text("Address") },
        )
        AdaptiveTextField(
            keyboardType = KeyboardType.Phone,
            title = { Text("Phone Number") },
            text = newPatient.phoneNumber,
            supportingText = phoneNumberError,
            changeSupportingText = { phoneNumberError = it },
            onTextChange = {
                if (it.length <= maxPhoneNumberChars) {
                    onNewPatientChange(newPatient.copy(phoneNumber = it))
                }
            },
            focusRequester = focusRequester,
            placeholder = { Text("Phone Number") }
        )
        AdaptiveTextField(
            keyboardType = KeyboardType.Email,
            title = { Text("Email") },
            text = newPatient.email,
            supportingText = emailError,
            changeSupportingText = { emailError = it },
            onTextChange = { onNewPatientChange(newPatient.copy(email = it)) },
            focusRequester = focusRequester,
            placeholder = { Text("Email") },
        )

        Spacer(modifier = Modifier.height(30.dp))

        AdaptiveTonalButton(
            onClick = {
                handleCreatePatient(
                    navigateBack = navigateBack,
                    newPatient = newPatient,
                    scope = scope,
                    isFormValid = isFormValid,
                    changeSsnError = { ssnError = it },
                    changeFirstNameError = { firstNameError = it },
                    changeLastNameError = { lastNameError = it },
                    changeAgeError = { ageError = it },
                    changeGenderError = { genderError = it },
                    changeAddressError = { addressError = it },
                    changePhoneNumberError = { phoneNumberError = it },
                    changeEmailError = { emailError = it },
                    changeDateOfBirthError = { dateOfBirthError = it },
                    changeFormError = { formError = it }
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
            Text(text = "Create Patient", fontSize = 16.sp)
        }
        if (formError.isNotBlank()) {
            Text(formError)
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
    val width = if (isCupertino) 300.dp else 270.dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
                        adaptation = {
                            cupertino {
                                colors = CupertinoCheckboxDefaults.colors(
                                    uncheckedBorderColor = cupertinoGray,
                                    checkedBorderColor = cupertinoGrayInactive
                                )
                            }
                        },
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

private fun handleCreatePatient(
    newPatient: Patient,
    navigateBack: () -> Unit,
    scope: CoroutineScope,
    isFormValid: Boolean,
    changeSsnError: (String) -> Unit,
    changeFirstNameError: (String) -> Unit,
    changeLastNameError: (String) -> Unit,
    changeAgeError: (String) -> Unit,
    changeGenderError: (String) -> Unit,
    changeAddressError: (String) -> Unit,
    changePhoneNumberError: (String) -> Unit,
    changeEmailError: (String) -> Unit,
    changeDateOfBirthError: (String) -> Unit,
    changeFormError: (String) -> Unit,
) {
    changeSsnError("")
    changeFirstNameError("")
    changeLastNameError("")
    changeAgeError("")
    changeGenderError("")
    changeAddressError("")
    changePhoneNumberError("")
    changeEmailError("")
    changeDateOfBirthError("")
    changeFormError("")

    if (isFormValid) {
        scope.launch {
            val createNewPatientResult = GlobalKtorClient.createNewPatient(newPatient)
            when (createNewPatientResult) {
                is CreatePatientResult.Error -> changeFormError(createNewPatientResult.message)
                CreatePatientResult.Success -> navigateBack()
            }
        }
    } else {
        newPatient.socialSecurityNumber.ifBlank {
            changeSsnError("This field can't be blank")
        }
        newPatient.firstName.ifBlank {
            changeFirstNameError("This field can't be blank")
        }
        newPatient.lastName.ifBlank {
            changeLastNameError("This field can't be blank")
        }
        newPatient.age.ifBlank {
            changeAgeError("This field can't be blank")
        }
        newPatient.gender.ifBlank {
            changeGenderError("This field can't be blank")
        }
        newPatient.address.ifBlank {
            changeAddressError("This field can't be blank")
        }
        newPatient.phoneNumber.ifBlank {
            changePhoneNumberError("This field can't be blank")
        }
        newPatient.email.ifBlank {
            changeEmailError("This field can't be blank")
        }
        newPatient.birthDate.ifBlank {
            changeDateOfBirthError("This field can't be blank")
        }
    }

    Logger.i(newPatient.toString(), tag = "Patient")
}