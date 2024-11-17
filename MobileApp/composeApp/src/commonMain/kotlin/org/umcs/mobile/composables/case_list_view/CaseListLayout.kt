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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import org.umcs.mobile.composables.case_list_view.doctor.CaseListDoctorFAB
import org.umcs.mobile.composables.case_list_view.patient.CaseListPatientFAB
import org.umcs.mobile.data.Case

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaseListLayout(
    navigateToCase: (Case) -> Unit,
    navigateBack: () -> Unit,
    navigateToShareUUID : (() -> Unit)? = null,
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
                navigateToAddNewPatientView = {},
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
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection)
        )
    }
}

fun fetchTestCases(): List<Case> {
    return listOf(
        Case(caseDetails = "Broken arm", stringDate = "2023-01-01", patientName = "John Doe", doctorName = "Dr. Smith"),
        Case(caseDetails = "Sprained ankle", stringDate = "2023-02-01", patientName = "Jane Smith", doctorName = "Dr. Johnson"),
        Case(
            caseDetails = "Fractured wrist",
            stringDate = "2023-03-01",
            patientName = "Alice Johnson",
            doctorName = "Dr. Williams"
        ),
        Case(
            caseDetails = "Dislocated shoulder",
            stringDate = "2023-04-01",
            patientName = "Bob Brownnnnnnnnnnnnnn",
            doctorName = "Dr. Brown"
        ),
        Case(caseDetails = "Broken leg", stringDate = "2023-05-01", patientName = "Charlie Davis", doctorName = "Dr. Davis"),
        Case(caseDetails = "Concussion", stringDate = "2023-06-01", patientName = "Diana Evans", doctorName = "Dr. Wilson"),
        Case(caseDetails = "Broken ribs", stringDate = "2023-07-01", patientName = "Eve Foster", doctorName = "Dr. Taylor"),
        Case(caseDetails = "Knee injury", stringDate = "2023-08-01", patientName = "Frank Green", doctorName = "Dr. Anderson"),
        Case(caseDetails = "Back pain", stringDate = "2023-09-01", patientName = "Grace Harris", doctorName = "Dr. Martinez"),
        Case(caseDetails = "Twisted ankle", stringDate = "2023-10-01", patientName = "Henry Irving", doctorName = "Dr. Thomas")
    )
}