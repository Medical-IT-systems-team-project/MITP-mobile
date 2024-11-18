package org.umcs.mobile.composables.case_list_view.patient.share_uuid_view

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.umcs.mobile.composables.shared.BackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareUUIDTopBar(navigateBack: () -> Unit) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            BackButton(navigateBack)
        },
        title = { Text("Your UUID") }
    )
}

