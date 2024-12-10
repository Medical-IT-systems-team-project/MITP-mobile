package org.umcs.mobile.composables.case_list_view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.theapache64.rebugger.Rebugger
import org.umcs.mobile.data.Case

@Composable
fun CaseViewContent(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    cases: List<Case>,
    listState: LazyListState,
    onCaseClicked: (Case) -> Unit,
    showPatientName: Boolean,
) {
    LazyColumn(
        modifier = modifier,
        state = listState,
        contentPadding = contentPadding,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        items(items = cases, key = { case -> case.patientName }) { case ->
            AdaptiveCase(
                onCaseClicked = onCaseClicked,
                currentCase = case,
                showPatientName = showPatientName,
            )
        }
        item { Spacer(modifier.height(5.dp)) }
    }

    Rebugger(
        trackMap = mapOf(
            "modifier" to modifier,
            "contentPadding" to contentPadding,
            "cases" to cases,
            "listState" to listState,
            "navigateToCase" to onCaseClicked,
            "isDoctor" to showPatientName,
            "Alignment.CenterHorizontally" to Alignment.CenterHorizontally,
            "Arrangement.spacedBy(30.dp)" to Arrangement.spacedBy(30.dp),
        ),
    )
}