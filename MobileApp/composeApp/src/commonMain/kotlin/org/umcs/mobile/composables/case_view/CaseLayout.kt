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
import com.slapps.cupertino.adaptive.icons.AdaptiveIcons
import com.slapps.cupertino.adaptive.icons.AddCircle
import org.umcs.mobile.composables.shared.AdaptiveFAB
import org.umcs.mobile.composables.shared.AppTopBar
import org.umcs.mobile.data.Case

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaseLayout(case: Case, navigateBack: () -> Unit, isDoctor: Boolean) {
    var currentTab by remember { mutableStateOf(CaseScreens.INFO) }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            if (isDoctor) {
                CaseLayoutFAB(
                    currentTab = currentTab,
                    infoOnClick = {},
                    treatmentsOnClick = {},
                    medicationOnClick = {}
                )
            }
        },
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
        when (currentTab) {
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

@Composable
fun CaseLayoutFAB(
    currentTab: CaseScreens,
    infoOnClick: () -> Unit,
    treatmentsOnClick: () -> Unit,
    medicationOnClick: () -> Unit,
) {
    when (currentTab) {
        CaseScreens.INFO -> {
            AdaptiveFAB(
                onClick = infoOnClick,
                iconVector = AdaptiveIcons.Outlined.AddCircle
            )
        }

        CaseScreens.TREATMENTS -> {
            AdaptiveFAB(
                onClick = treatmentsOnClick,
                iconVector = AdaptiveIcons.Outlined.AddCircle
            )
        }

        CaseScreens.MEDICATIONS -> {
            AdaptiveFAB(
                onClick = medicationOnClick,
                iconVector = AdaptiveIcons.Outlined.AddCircle
            )
        }
    }
}




