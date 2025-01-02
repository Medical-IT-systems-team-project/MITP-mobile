package org.umcs.mobile.composables.case_list_view.doctor

import AppViewModel
import androidx.compose.runtime.Composable
import org.koin.compose.viewmodel.koinViewModel
import org.umcs.mobile.composables.shared.new_uuid.NewUUIDScreen
import org.umcs.mobile.network.AllMedicalCasesResult
import org.umcs.mobile.network.AllPatientsResult
import org.umcs.mobile.network.GlobalKtorClient
import org.umcs.mobile.network.ImportPatientResult

@Composable
fun ImportNewPatient(navigateBack: () -> Unit, viewmodel: AppViewModel = koinViewModel()) {

    NewUUIDScreen(
        onSuccessButtonClick = { patientAccessID ->
            val importPatientResult = GlobalKtorClient.importPatient(patientAccessID)

            when (importPatientResult) {
                is ImportPatientResult.Error -> importPatientResult.message

                ImportPatientResult.Success -> {
                    val patientsResult = GlobalKtorClient.getAllDoctorPatients()
                    val casesResult = GlobalKtorClient.getAllMedicalCasesAsDoctor()

                    if (patientsResult is AllPatientsResult.Success) {
                        viewmodel.setPatients(patientsResult.patients)
                    }
                    if (casesResult is AllMedicalCasesResult.Success) {
                        viewmodel.setMedicalCases(casesResult.cases)
                    }
                    navigateBack()
                    null
                }
            }
        },
        title = "Import Patient",
        navigateBack = navigateBack,
        label = "Patient's Access ID "
    )
}