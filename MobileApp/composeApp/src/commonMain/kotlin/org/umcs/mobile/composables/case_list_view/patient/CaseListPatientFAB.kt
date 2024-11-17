package org.umcs.mobile.composables.case_list_view.patient

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import mobileapp.composeapp.generated.resources.Res
import mobileapp.composeapp.generated.resources.share_qr
import org.umcs.mobile.composables.shared.AppFab

@Composable
fun CaseListPatientFAB(modifier: Modifier = Modifier, shareUUID: () -> Unit) {
    AppFab(modifier = modifier, iconResource = Res.drawable.share_qr, onClick = shareUUID)
}