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
import org.umcs.mobile.navigation.Case

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaseListLayout(navigateToCase: (Case) -> Unit, navigateBack: () -> Unit) {
    val testValues = remember { fetchTestCases() }
    val contentState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { CaseViewTopBar(scrollBehavior) },
        floatingActionButton = { CaseListFAB(Modifier.offset(y= (-20).dp)) },
    ) { paddingValues ->
        CaseViewContent(
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
        Case(uuid = "test-uuid-1", stringDate = "2023-01-01"),
        Case(uuid = "test-uuid-2", stringDate = "2023-02-01"),
        Case(uuid = "test-uuid-3", stringDate = "2023-03-01"),
        Case(uuid = "test-uuid-4", stringDate = "2023-04-01"),
        Case(uuid = "test-uuid-5", stringDate = "2023-05-01"),
        Case(uuid = "test-uuid-6", stringDate = "2023-06-01"),
        Case(uuid = "test-uuid-7", stringDate = "2023-07-01"),
        Case(uuid = "test-uuid-8", stringDate = "2023-08-01"),
        Case(uuid = "test-uuid-9", stringDate = "2023-09-01"),
        Case(uuid = "test-uuid-10", stringDate = "2023-10-01")
    )
}