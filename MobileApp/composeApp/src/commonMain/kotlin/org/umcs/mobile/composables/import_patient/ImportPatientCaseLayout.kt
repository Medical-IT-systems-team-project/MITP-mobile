package org.umcs.mobile.composables.import_patient

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.umcs.mobile.composables.case_list_view.CaseViewContent
import org.umcs.mobile.composables.shared.AppTopBar
import org.umcs.mobile.data.Case
import org.umcs.mobile.data.CaseStatus
import org.umcs.mobile.data.Patient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportPatientCaseLayout(
    navigateBack: () -> Unit,
    patient: Patient,
    onCaseClicked: (Case) -> Unit,
    cases: List<Case> = fetchTestCases(),
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopBar(
                title = patient.getFullName(),
                navigateBack = navigateBack,
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
            )
        }
    ) { contentPadding ->
        CaseViewContent(
            contentPadding = contentPadding,
            modifier = modifier,
            cases = cases,
            listState = listState,
            onCaseClicked = onCaseClicked,
            showPatientName = false,
        )
    }
}

fun fetchTestCases() : List<Case> {
    return listOf(
        Case(
            patientName = "Sergio Jackson",
            status = CaseStatus.COMPLETED,
            admissionReason = "maiorum",
            admissionDate = "ultrices",
            description = "solet",
            createdBy = "fugit",
            attendingDoctor = "nascetur",
            medications = listOf(),
            treatments = listOf(),
            allowedDoctors = listOf()
        )
    )
}