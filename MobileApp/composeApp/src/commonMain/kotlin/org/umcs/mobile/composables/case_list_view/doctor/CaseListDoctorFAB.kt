package org.umcs.mobile.composables.case_list_view.doctor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mobileapp.composeapp.generated.resources.Res
import mobileapp.composeapp.generated.resources.add_case
import mobileapp.composeapp.generated.resources.add_patient
import org.umcs.mobile.composables.shared.AppFab

@Composable
fun CaseListDoctorFAB(
    modifier: Modifier = Modifier,
    navigateToAddNewPatientView: ()-> Unit,
    navigateToAddNewCaseView: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ){
        AppFab(iconResource = Res.drawable.add_case, onClick = navigateToAddNewCaseView)
        AppFab(iconResource = Res.drawable.add_patient, onClick = navigateToAddNewPatientView)
    }
}