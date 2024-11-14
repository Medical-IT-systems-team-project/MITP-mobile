package org.umcs.mobile.composables.case_list_view

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import mobileapp.composeapp.generated.resources.Res
import mobileapp.composeapp.generated.resources.caretrack
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaseViewTopBar(scrollBehavior: TopAppBarScrollBehavior) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Icon(
                painter = painterResource(Res.drawable.caretrack),
                contentDescription = null
            )
        }
    )
}