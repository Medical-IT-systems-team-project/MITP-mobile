package org.umcs.mobile.composables.case_list_view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import co.touchlab.kermit.Logger
import mobileapp.composeapp.generated.resources.Res
import mobileapp.composeapp.generated.resources.add_case
import mobileapp.composeapp.generated.resources.add_patient
import mobileapp.composeapp.generated.resources.import_patient
import org.umcs.mobile.composables.case_list_view.doctor.CaseListDoctorFAB
import org.umcs.mobile.composables.case_list_view.patient.CaseListPatientFAB
import org.umcs.mobile.data.Patient

@Composable
fun CaseListLayoutFAB(
    isDoctor: Boolean,
    fabOffset: Modifier,
    navigateToAddNewPatient: (() -> Unit)?,
    navigateToAddNewCase: (() -> Unit)?,
    currentTab: CaseListScreens,
    navigateToShareUUID: ((Patient) -> Unit)?,
    currentPatient: Patient
) {
    when {
        isDoctor && currentTab == CaseListScreens.CASES -> {
            CaseListDoctorFAB(
                modifier = fabOffset,
                lowerFabFunction = navigateToAddNewCase!!,
                lowerIcon = Res.drawable.add_case
            )
        }

        isDoctor && currentTab == CaseListScreens.PATIENTS -> {
            CaseListDoctorFAB(
                modifier = fabOffset,
                upperFabFunction = {},
                lowerFabFunction = navigateToAddNewPatient!!,
                upperIcon = Res.drawable.import_patient,
                lowerIcon = Res.drawable.add_patient,
            )
        }

        !isDoctor -> {
            CaseListPatientFAB(
                modifier = fabOffset,
                shareUUID = { navigateToShareUUID!!(currentPatient) },
            )
        }
    }
}
