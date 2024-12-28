package org.umcs.mobile.composables.new_medication_view

import AppViewModel
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
import org.koin.compose.viewmodel.koinViewModel
import org.umcs.mobile.composables.shared.AppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewMedicationLayout(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    medicalCaseID: Int,
    viewModel: AppViewModel = koinViewModel(),
) {
    var newMedication by remember { mutableStateOf(Medication()) }
    val startDatePickerState = rememberModalBottomSheetState()
    val endDatePickerState = rememberModalBottomSheetState()
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
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
                title = "New Medication",
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
            )
        }
    ) { paddingValues ->
        NewMedicationContent(
            paddingValues = paddingValues,
            showStartDatePicker = showStartDatePicker,
            showEndDatePicker = showEndDatePicker,
            startDatePickerState = startDatePickerState,
            endDatePickerState = endDatePickerState,
            onShowStartDatePickerChange = { showStartDatePicker = it },
            onShowEndDatePickerChange = { showEndDatePicker = it },
            onNewMedicationChange = { newMedication = it },
            newMedication = newMedication,
            focusRequester = focusRequester,
            medicalCaseID = medicalCaseID,
            doctorID = viewModel.doctorID,
            modifier = Modifier.verticalScroll(state = scrollState)
        )
    }
}


data class Medication(
    val name: String = "",
    val startDate : String = "",
    val endDate : String = "",
    val details : String = "",
    val dosageForm : String = "",
    val frequency : String = "",
    val strength : String = "",
    val unit : String = ""
)
