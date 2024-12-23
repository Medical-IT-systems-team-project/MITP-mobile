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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.slapps.cupertino.adaptive.Theme
import org.koin.compose.viewmodel.koinViewModel
import org.umcs.mobile.composables.case_list_view.doctor.PatientListContent
import org.umcs.mobile.data.Case
import org.umcs.mobile.data.CaseStatus
import org.umcs.mobile.data.Patient
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
    val isMaterial = when (determineTheme()) {
        Theme.Cupertino -> false
        Theme.Material3 -> true
    }
    val fabOffset = Modifier.offset(y = (-40).dp)
    var currentTab by remember { mutableStateOf(CaseListScreens.CASES) }
    val testValues = remember { fetchTestCases() }
    val caseListState = rememberLazyListState()
    val patientListState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val currentPatient = Patient("SSN","Michas")

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
                navigateToAddNewCase = navigateToAddNewCase,
                currentTab = currentTab,
                navigateToShareUUID = navigateToSharePatientUUID,
                currentPatient = currentPatient,
                navigateToImportNewPatient = navigateToImportNewPatient
            )
        },
    ) { paddingValues ->
        when (currentTab) {
            CaseListScreens.CASES -> {
                CaseViewContent(
                    showPatientName = isDoctor,
                    onCaseClicked = navigateToCase,
                    contentPadding = paddingValues,
                    cases = testValues,
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
                    //TODO : FETCH DOCTOR'S PATIENTS
                )
            }
        }
    }
}

val loremIpsum =
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."

fun fetchTestCases(): List<Case> {
    return listOf(
        Case(
            patientName = "John Doe",
            status = CaseStatus.COMPLETED,
            admissionReason = "Broken arm",
            admissionDate = "2023-01-01",
            description = loremIpsum,
            createdBy = "Admin",
            attendingDoctor = "Dr. Smith"
        ),
        Case(
            patientName = "Jane Smith",
            status = CaseStatus.ONGOING,
            admissionReason = "Sprained ankle",
            admissionDate = "2023-02-01",
            description = loremIpsum,
            createdBy = "Admin",
            attendingDoctor = "Dr. Johnson"
        ),
        Case(
            patientName = "Alice Johnson",
            status = CaseStatus.ONGOING,
            admissionReason = "Fractured wrist",
            admissionDate = "2023-03-01",
            description = loremIpsum,
            createdBy = "Admin",
            attendingDoctor = "Dr. Williams"
        ),
        Case(
            patientName = "Bob Brown",
            status = CaseStatus.ONGOING,
            admissionReason = "Dislocated shoulder",
            admissionDate = "2023-04-01",
            description = loremIpsum,
            createdBy = "Admin",
            attendingDoctor = "Dr. Brown"
        ),
        Case(
            patientName = "Charlie Davis",
            status = CaseStatus.ONGOING,
            admissionReason = "Broken leg",
            admissionDate = "2023-05-01",
            description = loremIpsum,
            createdBy = "Admin",
            attendingDoctor = "Dr. Davis"
        ),
        Case(
            patientName = "Diana Evans",
            status = CaseStatus.ONGOING,
            admissionReason = "Concussion",
            admissionDate = "2023-06-01",
            description = loremIpsum,
            createdBy = "Admin",
            attendingDoctor = "Dr. Wilson"
        ),
        Case(
            patientName = "Eve Foster",
            status = CaseStatus.ONGOING,
            admissionReason = "Broken ribs",
            admissionDate = "2023-07-01",
            description = loremIpsum,
            createdBy = "Admin",
            attendingDoctor = "Dr. Taylor"
        ),
        Case(
            patientName = "Frank Green",
            status = CaseStatus.ONGOING,
            admissionReason = "Knee injury",
            admissionDate = "2023-08-01",
            description = loremIpsum,
            createdBy = "Admin",
            attendingDoctor = "Dr. Anderson"
        ),
        Case(
            patientName = "Grace Harris",
            status = CaseStatus.ONGOING,
            admissionReason = "Back pain",
            admissionDate = "2023-09-01",
            description = loremIpsum,
            createdBy = "Admin",
            attendingDoctor = "Dr. Martinez"
        ),
        Case(
            patientName = "Henry Irving",
            status = CaseStatus.ONGOING,
            admissionReason = "Twisted ankle",
            admissionDate = "2023-10-01",
            description = loremIpsum,
            createdBy = "Admin",
            attendingDoctor = "Dr. Thomas"
        )
    )
}