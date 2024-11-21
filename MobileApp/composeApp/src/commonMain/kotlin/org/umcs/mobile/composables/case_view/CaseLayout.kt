package org.umcs.mobile.composables.case_view

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
import org.umcs.mobile.composables.shared.AppTopBar
import org.umcs.mobile.data.Case

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaseLayout(case: Case, navigateBack: () -> Unit) {
    var currentTab by remember { mutableStateOf(CaseScreens.INFO) }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppTopBar(
                scrollBehavior = scrollBehavior,
                navigateBack = navigateBack,
                title = case.caseDetails
            )
        },
        bottomBar = {
            CaseViewBottomBar(
                currentTab = currentTab,
                navigateToInfo = { currentTab = CaseScreens.INFO },
                navigateToTreatments = { currentTab = CaseScreens.TREATMENTS },
                navigateToMedication = { currentTab = CaseScreens.MEDICATIONS }
            )
        }
    ) { paddingValues ->
        when(currentTab){
            CaseScreens.INFO -> InfoContent(
                paddingValues = paddingValues,
                case = case
            )
            CaseScreens.TREATMENTS -> TreatmentsContent(
                paddingValues = paddingValues,
                case = case
            )
            CaseScreens.MEDICATIONS -> MedicationContent(
                paddingValues = paddingValues,
                case = case
            )
        }
    }
}





