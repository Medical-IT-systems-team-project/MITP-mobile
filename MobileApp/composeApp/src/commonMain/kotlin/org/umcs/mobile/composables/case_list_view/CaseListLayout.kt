package org.umcs.mobile.composables.case_list_view

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
import androidx.compose.ui.unit.dp
import org.umcs.mobile.composables.case_list_view.doctor.PatientListContent
import org.umcs.mobile.data.Case
import org.umcs.mobile.data.Patient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaseListLayout(
    navigateToCase: (Case) -> Unit,
    navigateBack: () -> Unit,
    navigateToAddNewPatient: (() -> Unit)? = null,
    navigateToAddNewCase: (() -> Unit)? = null,
    navigateToImportPatientCase: ((Patient) -> Unit)? = null,
    navigateToSharePatientUUID: ((Patient) -> Unit)? = null,
    isDoctor: Boolean = true
) {
    val fabOffset = Modifier.offset(y = (-40).dp)
    var currentTab by remember { mutableStateOf(CaseListScreens.CASES) }
    val testValues = remember { fetchTestCases() }
    val caseListState = rememberLazyListState()
    val patientListState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val currentPatient = Patient("John","Doevski")

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { CaseViewTopBar(scrollBehavior, isDoctor) },
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
                navigateToShareUUID = navigateToImportPatientCase,
                currentPatient = currentPatient
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
                    listState = caseListState,
                    modifier = Modifier.fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                )
            }

            CaseListScreens.PATIENTS -> {
                PatientListContent(
                    listState = patientListState,
                    contentPadding = paddingValues,
                    modifier = Modifier.fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
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
            caseDetails = "Broken arm",
            stringDate = "2023-01-01",
            patientName = "John Doe",
            doctorName = "Dr. Smith",
            description = loremIpsum
        ),
        Case(
            caseDetails = "Sprained ankle",
            stringDate = "2023-02-01",
            patientName = "Jane Smith",
            doctorName = "Dr. Johnson",
            description = loremIpsum
        ),
        Case(
            caseDetails = "Fractured wrist",
            stringDate = "2023-03-01",
            patientName = "Alice Johnson",
            doctorName = "Dr. Williams",
            description = loremIpsum
        ),
        Case(
            caseDetails = "Dislocated shoulder",
            stringDate = "2023-04-01",
            patientName = "Bob Brownnnnnnnnnnnnnn",
            doctorName = "Dr. Brown",
            description = loremIpsum
        ),
        Case(
            caseDetails = "Broken leg",
            stringDate = "2023-05-01",
            patientName = "Charlie Davis",
            doctorName = "Dr. Davis",
            description = loremIpsum
        ),
        Case(
            caseDetails = "Concussion",
            stringDate = "2023-06-01",
            patientName = "Diana Evans",
            doctorName = "Dr. Wilson",
            description = loremIpsum
        ),
        Case(
            caseDetails = "Broken ribs",
            stringDate = "2023-07-01",
            patientName = "Eve Foster",
            doctorName = "Dr. Taylor",
            description = loremIpsum
        ),
        Case(
            caseDetails = "Knee injury",
            stringDate = "2023-08-01",
            patientName = "Frank Green",
            doctorName = "Dr. Anderson",
            description = loremIpsum
        ),
        Case(
            caseDetails = "Back pain",
            stringDate = "2023-09-01",
            patientName = "Grace Harris",
            doctorName = "Dr. Martinez",
            description = loremIpsum
        ),
        Case(
            caseDetails = "Twisted ankle",
            stringDate = "2023-10-01",
            patientName = "Henry Irving",
            doctorName = "Dr. Thomas",
            description = loremIpsum
        )
    )
}