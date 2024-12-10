package org.umcs.mobile.composables.new_patient_view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import org.umcs.mobile.composables.shared.AppTopBar
import org.umcs.mobile.data.Patient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPatientLayout(navigateBack: () -> Unit) {
    var newPatient by remember { mutableStateOf(Patient()) }
    var formState by remember { mutableStateOf(PatientFormState()) } //TODO : Handle errors from client
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val ssnMaxChars = 11

    Scaffold(
        modifier = Modifier.fillMaxSize().clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = focusManager::clearFocus
        ),
        topBar = {
            AppTopBar(
                navigateBack = navigateBack,
                title = "New Patient",
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
            )
        }
    ) { paddingValues ->
        NewPatientContent(
            paddingValues = paddingValues,
            newPatient = newPatient,
            formState = formState,
            focusRequester = focusRequester,
            onFirstNameChange = { newPatient = newPatient.copy(firstName = it) },
            onLastNameChange = { newPatient = newPatient.copy(lastName = it) },
            onGenderChange = { newPatient = newPatient.copy(gender = it) },
            onAgeChange = { newPatient = newPatient.copy(age = it) },
            onSocialSecurityNumberChange = {
                if (it.length <= ssnMaxChars) {
                    newPatient = newPatient.copy(socialSecurityNumber = it)
                }
            },
            onDateOfBirthChange = { newPatient = newPatient.copy(dateOfBirth = it) }
        )
    }
}

data class PatientFormState(
    var firstNameError: String = "",
    var lastNameError: String = "",
    var socialSecurityNumberError: String = "",
    var genderError: String = "",
    var dateOfBirthError: String = "",
    var ageError: String = "",
    var error: String = ""
)




