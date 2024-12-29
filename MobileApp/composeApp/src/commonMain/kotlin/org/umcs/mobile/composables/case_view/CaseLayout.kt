package org.umcs.mobile.composables.case_view

import AppViewModel
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.slapps.cupertino.adaptive.icons.AdaptiveIcons
import com.slapps.cupertino.adaptive.icons.AddCircle
import com.slapps.cupertino.icons.CupertinoIcons
import com.slapps.cupertino.icons.outlined.Checkmark
import com.slapps.cupertino.icons.outlined.Cross
import org.koin.compose.viewmodel.koinViewModel
import org.umcs.mobile.composables.shared.AdaptiveFAB
import org.umcs.mobile.composables.shared.AppTopBar
import org.umcs.mobile.data.CaseStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaseLayout(
    caseId: Int,
    navigateBack: () -> Unit,
    isDoctor: Boolean,
    navigateToNewTreatment: (Int) -> Unit,
    navigateToNewMedication: (Int) -> Unit,
    viewModel: AppViewModel = koinViewModel()
) {
    val caseList by viewModel.medicalCaseList.collectAsStateWithLifecycle()
    val case = caseList.first()
    var currentTab by remember { mutableStateOf(CaseScreens.INFO) }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            if (isDoctor) {
                CaseLayoutFAB(
                    currentTab = currentTab,
                    infoOnClick = {},
                    treatmentsOnClick = { navigateToNewTreatment(1) }, //TODO : change that number to case ID
                    medicationOnClick = { navigateToNewMedication(1) }
                )
            }
        },
        topBar = {
            AppTopBar(
                scrollBehavior = scrollBehavior,
                navigateBack = navigateBack,
                title = case.admissionReason,
                actions = { CaseStatusIcon(case.status) }
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
fun CaseStatusIcon(status : CaseStatus) {
    val statusVector = when(status){
        CaseStatus.ONGOING -> CupertinoIcons.Outlined.Cross
        CaseStatus.COMPLETED -> CupertinoIcons.Outlined.Checkmark
    }

    Icon(
        imageVector = statusVector,
        contentDescription = null
    )
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
            /*AdaptiveFAB(
                onClick = infoOnClick,
                iconVector = AdaptiveIcons.Outlined.AddCircle
            )*/
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




