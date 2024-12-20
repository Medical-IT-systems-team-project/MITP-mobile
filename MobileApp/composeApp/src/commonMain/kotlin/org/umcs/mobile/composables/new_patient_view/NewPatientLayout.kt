package org.umcs.mobile.composables.new_patient_view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
fun NewPatientLayout(navigateBack: () -> Unit) {
    var newPatient by remember { mutableStateOf(Patient()) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberModalBottomSheetState()
    val interactionSource = remember { MutableInteractionSource() }
    val scrollState = rememberScrollState()

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
            focusRequester = focusRequester,
            showDatePicker = showDatePicker,
            datePickerState = datePickerState,
            onShowDatePickerChange = {showDatePicker = it},
            onNewPatientChange = {newPatient =it},
            modifier = Modifier.verticalScroll(state = scrollState)
        )
    }
}





