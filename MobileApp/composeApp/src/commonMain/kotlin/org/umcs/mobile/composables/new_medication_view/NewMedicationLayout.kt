package org.umcs.mobile.composables.new_medication_view

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewMedicationLayout(navigateBack: () -> Unit, modifier: Modifier = Modifier, medicalCaseID: Int) {
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
                title = "New Treatment",
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
            )
        }
    ) { paddingValues ->

    }
}
