package org.umcs.mobile.composables.login

import AppViewModel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.compose.viewmodel.koinViewModel
import org.umcs.mobile.composables.shared.new_uuid.NewUUIDScreen
import org.umcs.mobile.network.GlobalKtorClient
import org.umcs.mobile.network.LoginResult

@Composable
fun PatientLoginLayout(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = koinViewModel(),
    navigateToCaseList: () -> Unit,
) {
    val title = "Login as Patient"
    val label = "Access ID"

    NewUUIDScreen(
        onSuccessButtonClick = { passedAccessId ->
            val networkCallResult = GlobalKtorClient.loginAsPatient(passedAccessId)

            when (networkCallResult) {
                is LoginResult.Error -> networkCallResult.message
                is LoginResult.Success -> {
                    viewModel.setPatient(networkCallResult.patient)
                    navigateToCaseList()
                    null
                }
            }
        },
        title = title,
        label = label,
    )
}