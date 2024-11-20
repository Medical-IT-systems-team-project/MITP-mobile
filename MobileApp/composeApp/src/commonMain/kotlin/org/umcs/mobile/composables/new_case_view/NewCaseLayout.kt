package org.umcs.mobile.composables.new_case_view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
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
fun NewCaseLayout(navigateBack: () -> Unit, modifier: Modifier = Modifier) {
    var newCase by remember { mutableStateOf(MedicalCase()) }
    var formState by remember { mutableStateOf(MedicalCaseFormState()) }
    var fullName = newCase.getPatientFullName()

    val patientPickerState = rememberModalBottomSheetState()
    var showPatientPicker by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    Scaffold(
        modifier = Modifier.fillMaxSize().clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = focusManager::clearFocus
        ),
        topBar = {
            AppTopBar(
                navigateBack = navigateBack,
                title = "New Case",
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
            )
        }
    ) { paddingValues ->
        NewCaseContent(
            paddingValues = paddingValues,
            showPatientPicker = showPatientPicker,
            showDatePicker = showDatePicker,
            patientPickerState = patientPickerState,
            newCase = newCase,
            formState = formState,
            fullName = fullName,
            focusRequester = focusRequester,
            focusManager = focusManager,
            onNewCaseChange = { newCase = it },
            onShowPatientPickerChange = { showPatientPicker = it },
            onShowDatePickerChange = { showDatePicker = it }
        )
    }
}

fun patientList() = listOf(
    Patient(firstName = "John", lastName = "Doe", socialSecurityNumber = "12345678901"),
    Patient(firstName = "Jane", lastName = "Smith", socialSecurityNumber = "98765432109"),
    Patient(firstName = "Alice", lastName = "Johnson", socialSecurityNumber = "45678901234"),
    Patient(firstName = "Bob", lastName = "Williams", socialSecurityNumber = "78901234567"),
    Patient(firstName = "Emma", lastName = "Brown", socialSecurityNumber = "32109876543"),
    Patient(firstName = "Michael", lastName = "Davis", socialSecurityNumber = "65432109876"),
    Patient(firstName = "Sarah", lastName = "Wilson", socialSecurityNumber = "89012345678"),
    Patient(firstName = "David", lastName = "Taylor", socialSecurityNumber = "23456789012"),
    Patient(firstName = "Emily", lastName = "Anderson", socialSecurityNumber = "56789012345"),
    Patient(firstName = "James", lastName = "Martinez", socialSecurityNumber = "90123456789")
)
data class MedicalCase(
    var patient: Patient = Patient(),
    var admissionDate: String = "",
    var doctor: String = "",
    var details: String = "",
    var diagnosis: String = "",
) {
    fun getPatientFullName(): String {
        return patient.getFullName()
    }
}

data class MedicalCaseFormState(
    var patientError: String = "",
    var admissionDateError: String = "",
    var doctorError: String = "",
    var detailsError: String = "",
    var diagnosisError: String = "",
    var error: String = ""
)