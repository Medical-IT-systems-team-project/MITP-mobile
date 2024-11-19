package org.umcs.mobile.composables.case_list_view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import mobileapp.composeapp.generated.resources.Res
import mobileapp.composeapp.generated.resources.add_case
import mobileapp.composeapp.generated.resources.add_patient
import org.umcs.mobile.composables.case_list_view.doctor.CaseListDoctorFAB
import org.umcs.mobile.composables.case_list_view.patient.CaseListPatientFAB

@Composable
fun CaseListLayoutFAB(
    isDoctor: Boolean,
    fabOffset: Modifier,
    navigateToAddNewPatient: (() -> Unit)?,
    navigateToAddNewCase: (() -> Unit)?,
    currentTab: CaseListScreens,
    navigateToShareUUID: (() -> Unit)?
) {
    when {
        isDoctor && currentTab == CaseListScreens.CASES -> {
            CaseListDoctorFAB(
                modifier = fabOffset,
                navigateToAddNewPatientView = navigateToAddNewPatient!!,
                navigateToAddNewCaseView = navigateToAddNewCase!!,
                upperIcon = Res.drawable.add_case,
                lowerIcon = Res.drawable.add_patient
            )
        }

        isDoctor && currentTab == CaseListScreens.PATIENTS -> {
            CaseListDoctorFAB(
                modifier = fabOffset,
                navigateToAddNewPatientView = navigateToAddNewPatient!!,
                navigateToAddNewCaseView = navigateToAddNewCase!!,
                upperIcon = Res.drawable.add_patient,
                lowerIcon = Res.drawable.add_case
            )
        }

        !isDoctor -> {
            CaseListPatientFAB(
                modifier = fabOffset,
                shareUUID = navigateToShareUUID!!,
            )
        }
    }
}
