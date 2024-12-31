package org.umcs.mobile.composables.case_list_view

import AppViewModel
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import com.slapps.cupertino.adaptive.Theme
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.umcs.mobile.composables.case_list_view.doctor.PatientListContent
import org.umcs.mobile.data.Case
import org.umcs.mobile.data.Patient
import org.umcs.mobile.network.AllUnassignedPatientsResult
import org.umcs.mobile.network.GlobalKtorClient
import org.umcs.mobile.theme.determineTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaseListLayout(
    navigateToCase: (Case) -> Unit,
    navigateBack: () -> Unit,
    navigateToAddNewPatient: (() -> Unit)? = null,
    navigateToAddNewCase: (() -> Unit)? = null,
    navigateToImportPatientCase: ((Patient) -> Unit)? = null,
    navigateToImportNewPatient: (() -> Unit)? = null,
    navigateToSharePatientUUID: ((Patient) -> Unit)? = null,
    viewModel: AppViewModel = koinViewModel(),
    isDoctor: Boolean = true,
) {
    val caseListScope = rememberCoroutineScope()
    val isMaterial = when (determineTheme()) {
        Theme.Cupertino -> false
        Theme.Material3 -> true
    }
    val fabOffset = Modifier.offset(y = (-40).dp)
    var currentTab by remember { mutableStateOf(CaseListScreens.CASES) }
    val caseListState = rememberLazyListState()
    val patientListState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val currentPatient = viewModel.patient

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, dragAmount ->
                    change.consume()
                    if (dragAmount < 0) {
                        if (isDoctor && currentTab != CaseListScreens.PATIENTS) {
                            currentTab = CaseListScreens.PATIENTS
                        }
                    } else if (dragAmount > 0) {
                        if (isDoctor && currentTab != CaseListScreens.CASES) {
                            currentTab = CaseListScreens.CASES
                        }
                    }
                }
            },
        topBar = {
            MyAdaptiveTopBar(
                scrollBehavior = scrollBehavior,
                isDoctor = isDoctor,
                navigationIcon = {})
        },
        bottomBar = {
            if (isDoctor) {
                CaseListBottomBar(
                    currentTab = currentTab,
                    navigateToCases = { currentTab = CaseListScreens.CASES },
                    navigateToPatients = { currentTab = CaseListScreens.PATIENTS },
                )
            } else Unit
        },
        floatingActionButton = {
            CaseListLayoutFAB(
                isDoctor = isDoctor,
                fabOffset = fabOffset,
                navigateToAddNewPatient = navigateToAddNewPatient,
                currentTab = currentTab,
                navigateToShareUUID = navigateToSharePatientUUID,
                currentPatient = currentPatient,
                navigateToImportNewPatient = navigateToImportNewPatient,
                navigateToAddNewCase = navigateToAddNewCase,
                fetchUnassignedPatients = {
                    caseListScope.launch {
                        val getUnassignedPatient = GlobalKtorClient.getAllUnassignedPatients()
                        when (getUnassignedPatient) {
                            is AllUnassignedPatientsResult.Error -> Logger.e(getUnassignedPatient.message, tag = "Unassigned patients")
                            is AllUnassignedPatientsResult.Success -> viewModel.setUnassignedPatients(getUnassignedPatient.patients)
                        }
                    }
                }
            )
        },
    ) { paddingValues ->
        when (currentTab) {
            CaseListScreens.CASES -> {
                CaseViewContent(
                    showPatientName = isDoctor,
                    onCaseClicked = navigateToCase,
                    contentPadding = paddingValues,
                    modifier = Modifier.fillMaxSize().then(
                        if (isMaterial) Modifier.nestedScroll(scrollBehavior.nestedScrollConnection) else Modifier
                    ),
                    listState = caseListState,
                )
            }

            CaseListScreens.PATIENTS -> {
                PatientListContent(
                    listState = patientListState,
                    contentPadding = paddingValues,
                    modifier = Modifier.fillMaxSize().then(
                        if (isMaterial) Modifier.nestedScroll(scrollBehavior.nestedScrollConnection) else Modifier
                    ),
                    onImportPatientCase = navigateToImportPatientCase!!,
                    onShareUUID = navigateToSharePatientUUID!!,
                )
            }
        }
    }
}


