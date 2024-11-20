package org.umcs.mobile.composables.case_list_view.doctor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.umcs.mobile.composables.shared.AppFab

@Composable
fun CaseListDoctorFAB(
    modifier: Modifier = Modifier,
    upperFabFunction: () -> Unit,
    lowerFabFunction: () -> Unit,
    upperIcon: DrawableResource,
    lowerIcon: DrawableResource,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        AppFab(iconResource = upperIcon, onClick = upperFabFunction)
        AppFab(iconResource = lowerIcon, onClick = lowerFabFunction)
    }
}