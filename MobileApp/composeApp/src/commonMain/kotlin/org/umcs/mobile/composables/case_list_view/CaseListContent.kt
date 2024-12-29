package org.umcs.mobile.composables.case_list_view

import AppViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.touchlab.kermit.Logger
import com.theapache64.rebugger.Rebugger
import org.koin.compose.viewmodel.koinViewModel
import org.umcs.mobile.data.Case

@Composable
fun CaseViewContent(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    listState: LazyListState,
    onCaseClicked: (Case) -> Unit,
    showPatientName: Boolean,
    viewModel: AppViewModel = koinViewModel(),
) {
    val cases by viewModel.medicalCaseList.collectAsStateWithLifecycle()
    Logger.i(cases.toString(), tag = "CaseList")

    LazyColumn(
        modifier = modifier,
        state = listState,
        contentPadding = contentPadding,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        items(items = cases) { case ->
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