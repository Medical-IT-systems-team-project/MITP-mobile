package org.umcs.mobile.composables.login

import AppViewModel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import co.touchlab.kermit.Logger
import org.koin.compose.viewmodel.koinViewModel
import org.umcs.mobile.composables.shared.new_uuid.NewUUIDScreen
import org.umcs.mobile.network.AllMedicalCasesResult
import org.umcs.mobile.network.GlobalKtorClient
import org.umcs.mobile.network.PatientLoginResult

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
            val patientloginResult = GlobalKtorClient.loginAsPatient(passedAccessId)

            when (patientloginResult) {
                is PatientLoginResult.Error -> patientloginResult.message
                is PatientLoginResult.Success -> {
                    val loggedInPatient = patientloginResult.patient
                    viewModel.setPatient(loggedInPatient)

                    val getPatientMedicalCasesResult = GlobalKtorClient.getAllMedicalCasesAsPatient(loggedInPatient.accessId)
                    when(getPatientMedicalCasesResult){
                        is AllMedicalCasesResult.Error -> Logger.i(getPatientMedicalCasesResult.message)
                        is AllMedicalCasesResult.Success -> viewModel.setMedicalCases(getPatientMedicalCasesResult.cases)
                    }
                    navigateToCaseList()
                    null
                }
            }
        },
        title = title,
        label = label,
    )
}