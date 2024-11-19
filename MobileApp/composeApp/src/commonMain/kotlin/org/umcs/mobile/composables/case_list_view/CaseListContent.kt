package org.umcs.mobile.composables.case_list_view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.umcs.mobile.composables.case_list_view.doctor.CaseListItemForDoctor
import org.umcs.mobile.composables.case_list_view.patient.CaseListItemForPatient
import org.umcs.mobile.data.Case

@Composable
fun CaseViewContent(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    cases: List<Case>,
    listState: LazyListState,
    navigateToCase: (Case) -> Unit,
    isDoctor: Boolean,
) {
    LazyColumn(
        modifier = modifier,
        state = listState,
        contentPadding = contentPadding,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        items(cases) { case ->
            if (isDoctor) {
                CaseListItemForDoctor(
                    navigateToCase = navigateToCase,
                    currentCase = case,
                    modifier = Modifier.fillMaxWidth(0.8f).height(80.dp)
                )
            } else {
                CaseListItemForPatient(
                    navigateToCase = navigateToCase,
                    currentCase = case,
                    modifier = Modifier.fillMaxWidth(0.8f).height(80.dp)
                )
            }
        }
    }
}