package org.umcs.mobile.composables.case_view

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import org.umcs.mobile.composables.shared.BackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaseViewTopBar(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    title: String,
    scrollBehavior: TopAppBarScrollBehavior
) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            BackButton(navigateBack = navigateBack)
        }
    )
}