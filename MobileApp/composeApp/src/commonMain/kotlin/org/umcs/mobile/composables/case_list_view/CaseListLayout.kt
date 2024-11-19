package org.umcs.mobile.composables.case_list_view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.umcs.mobile.composables.case_list_view.doctor.CaseListDoctorFAB
import org.umcs.mobile.composables.case_list_view.patient.CaseListPatientFAB
import org.umcs.mobile.data.Case

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaseListLayout(
    navigateToCase: (Case) -> Unit,
    navigateBack: () -> Unit,
    navigateToAddNewPatient : (()->Unit)? = null,
    navigateToShareUUID: (() -> Unit)? = null,
    isDoctor: Boolean = true
) {
    val testValues = remember { fetchTestCases() }
    val contentState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val scaffoldFAB: @Composable () -> Unit = {
        val fabOffset = Modifier.offset(y = (-40).dp)

        if (isDoctor) {
            CaseListDoctorFAB(
                modifier = fabOffset,
                navigateToAddNewPatientView = navigateToAddNewPatient!!,
                navigateToAddNewCaseView = {}
            )
        } else {
            CaseListPatientFAB(
                modifier = fabOffset,
                shareUUID = navigateToShareUUID!!,
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { CaseViewTopBar(scrollBehavior, isDoctor) },
        floatingActionButton = scaffoldFAB,
    ) { paddingValues ->
        CaseViewContent(
            isDoctor = isDoctor,
            navigateToCase = navigateToCase,
            contentPadding = paddingValues,
            cases = testValues,
            listState = contentState,
            modifier = Modifier.fillMaxSize()
        )
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