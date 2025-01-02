@file:OptIn(ExperimentalAdaptiveApi::class)

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.touchlab.kermit.Logger
import com.slapps.cupertino.LocalContentColor
import com.slapps.cupertino.adaptive.AdaptiveAlertDialogNative
import com.slapps.cupertino.adaptive.AdaptiveCircularProgressIndicator
import com.slapps.cupertino.adaptive.ExperimentalAdaptiveApi
import com.slapps.cupertino.adaptive.icons.AdaptiveIcons
import com.slapps.cupertino.adaptive.icons.AddCircle
import com.slapps.cupertino.adaptive.icons.Close
import com.slapps.cupertino.cancel
import com.slapps.cupertino.default
import com.slapps.cupertino.icons.CupertinoIcons
import com.slapps.cupertino.icons.outlined.Checkmark
import com.slapps.cupertino.icons.outlined.Cross
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.umcs.mobile.composables.shared.AdaptiveFAB
import org.umcs.mobile.composables.shared.AppTopBar
import org.umcs.mobile.data.CaseStatus
import org.umcs.mobile.network.AllMedicalCasesResult
import org.umcs.mobile.network.AllPatientsResult
import org.umcs.mobile.network.CloseCaseResult
import org.umcs.mobile.network.GlobalKtorClient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaseLayout(
    caseId: Int,
    navigateBack: () -> Unit,
    isDoctor: Boolean,
    navigateToNewTreatment: (Int) -> Unit,
    navigateToNewMedication: (Int) -> Unit,
    viewmodel: AppViewModel = koinViewModel(),
) {
    val caseScope = rememberCoroutineScope()
    val caseList by viewmodel.medicalCaseList.collectAsStateWithLifecycle()
    val case = caseList.find { it.id == caseId }!!
    var currentTab by remember { mutableStateOf(CaseScreens.INFO) }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    var showAlertDialog by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    CloseCaseDialog(
        showAlertDialog = showAlertDialog,
        changeLoadingState = {loading = it},
        dismiss = {showAlertDialog = false},
        caseScope = caseScope,
        viewmodel = viewmodel,
        caseId = caseId,
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            if (isDoctor && case.status != CaseStatus.COMPLETED) {
                CaseLayoutFAB(
                    currentTab = currentTab,
                    infoOnClick = { showAlertDialog = true },
                    treatmentsOnClick = { navigateToNewTreatment(caseId) },
                    medicationOnClick = { navigateToNewMedication(caseId) }
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
        if(loading){
            AdaptiveCircularProgressIndicator(modifier = Modifier.fillMaxSize())
        }else {
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
}

@Composable
private fun CloseCaseDialog(
    showAlertDialog: Boolean,
    changeLoadingState: (Boolean) -> Unit,
    dismiss: () -> Unit,
    caseScope: CoroutineScope,
    viewmodel: AppViewModel,
    caseId: Int,
) {
    if (showAlertDialog) {
        AdaptiveAlertDialogNative(
            onDismissRequest = dismiss,
            title = "Close this case",
            message = "Are you sure you want to close this case ? All ongoing or planned treatments and medications will be cancelled",
        ) {
            cancel(
                onClick = dismiss,
                title = "Cancel"
            )
            default(
                onClick = {
                    changeLoadingState(true)
                    dismiss()
                    caseScope.launch {
                        val force =
                            viewmodel.areAnyTreatmentsOrMedicationsCompletedOrCancelled(caseId)
                        val closeCaseResult = GlobalKtorClient.closeCase(caseId, force)
                        when (closeCaseResult) {
                            is CloseCaseResult.Error -> Logger.i(closeCaseResult.message)
                            CloseCaseResult.Success -> {
                                val patientsResult = GlobalKtorClient.getAllDoctorPatients()
                                val casesResult = GlobalKtorClient.getAllMedicalCasesAsDoctor()

                                if (patientsResult is AllPatientsResult.Success) {
                                    viewmodel.setPatients(patientsResult.patients)
                                }
                                if (casesResult is AllMedicalCasesResult.Success) {
                                    viewmodel.setMedicalCases(casesResult.cases)
                                }
                            }
                        }
                        changeLoadingState(false)
                    }
                },
                title = "OK"
            )
        }
    }
}

@Composable
fun CaseStatusIcon(status: CaseStatus, tint : Color = LocalContentColor.current ) {
    val statusVector = when (status) {
        CaseStatus.ONGOING -> CupertinoIcons.Outlined.Cross
        CaseStatus.COMPLETED -> CupertinoIcons.Outlined.Checkmark
    }

    Icon(
        tint = tint,
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
            AdaptiveFAB(
                onClick = infoOnClick,
                iconVector = AdaptiveIcons.Outlined.Close
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




