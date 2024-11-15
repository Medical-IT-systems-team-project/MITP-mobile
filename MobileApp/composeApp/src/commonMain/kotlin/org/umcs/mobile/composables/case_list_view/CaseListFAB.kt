package org.umcs.mobile.composables.case_list_view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mobileapp.composeapp.generated.resources.Res
import mobileapp.composeapp.generated.resources.add_case
import mobileapp.composeapp.generated.resources.add_patient
import org.umcs.mobile.composables.shared.AppFab

@Composable
fun CaseListFAB(
    modifier : Modifier = Modifier,
    isDoctor: Boolean = true //default value for preview purpose
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        if (isDoctor){
            AppFab(iconResource = Res.drawable.add_patient, onClick = {})
        }

        AppFab(iconResource = Res.drawable.add_case, onClick = {})
    }
}



