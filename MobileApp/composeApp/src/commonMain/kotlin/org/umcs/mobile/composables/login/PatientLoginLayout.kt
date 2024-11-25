package org.umcs.mobile.composables.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.umcs.mobile.composables.shared.new_uuid.NewUUIDScreen

@Composable
fun PatientLoginLayout(
    modifier: Modifier = Modifier,
    navigateToCaseList : ()->Unit
) {
    val title = "Login as Patient"

    NewUUIDScreen(
        navigateToCaseList = navigateToCaseList,
        title = title
    )
}