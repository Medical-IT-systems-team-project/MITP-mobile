package org.umcs.mobile.composables.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.umcs.mobile.composables.shared.new_uuid.NewUUIDScreen
import org.umcs.mobile.network.GlobalKtorClient

@Composable
fun PatientLoginLayout(
    modifier: Modifier = Modifier,
    navigateToCaseList: () -> Unit,
) {
    val title = "Login as Patient"
    val label = "Access ID"

    NewUUIDScreen(
        onSuccessButtonClick = { accessID ->
            val errorMessage = GlobalKtorClient.loginAsPatient(accessID)
            if(errorMessage == null){
                navigateToCaseList()
            }
            errorMessage
        },
        title = title,
        label = label,
    )
}