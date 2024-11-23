package org.umcs.mobile.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import co.touchlab.kermit.Logger
import org.umcs.mobile.App
import org.umcs.mobile.composables.case_list_view.CaseListLayout
import org.umcs.mobile.composables.case_view.CaseLayout
import org.umcs.mobile.composables.import_patient.ImportPatientCaseLayout
import org.umcs.mobile.composables.login.ChooseProfileLayout
import org.umcs.mobile.composables.login.DoctorLoginLayout
import org.umcs.mobile.composables.login.PatientLoginLayout
import org.umcs.mobile.composables.new_case_view.NewCaseLayout
import org.umcs.mobile.composables.new_patient_view.NewPatientLayout
import org.umcs.mobile.composables.share_uuid_view.ShareUUIDLayout
import org.umcs.mobile.data.Case
import org.umcs.mobile.data.Patient
import org.umcs.mobile.theme.AppTheme


@Composable
fun NavigationHost(
    navController: NavHostController = rememberNavController(),
    loginDataStore: DataStore<Preferences>,
    testDataStore: DataStore<Preferences>,
) {
    //TODO : refactor this to serve the new navigation system
    AppTheme {
        NavHost(
            navController = navController,
            modifier = Modifier.fillMaxSize(),
            startDestination = Routes.HOME
        ) {
            composable(Routes.HOME) {
                App(
                    navController = navController,
                    testDataStore = testDataStore
                )
            }
            composable(Routes.CASE_LIST_DOCTOR) {
                CaseListLayout(
                    isDoctor = true,
                    navigateToCase = navController::navigate,
                    navigateBack = navController::navigateUp,
                    navigateToAddNewPatient = { navController.navigate(Routes.NEW_PATIENT) },
                    navigateToAddNewCase = { navController.navigate(Routes.NEW_CASE) },
                    navigateToImportPatientCase = { patient: Patient -> navController.navigate(patient) },
                    navigateToSharePatientUUID = {  }
                )
            }
            composable(Routes.NEW_PATIENT) {
                NewPatientLayout(
                    navigateBack = navController::navigateUp
                )
            }
            composable(Routes.NEW_CASE) {
                NewCaseLayout(
                    navigateBack = navController::navigateUp
                )
            }
            composable(Routes.CASE_LIST_PATIENT) {
                CaseListLayout(
                    isDoctor = false,
                    navigateToCase = navController::navigate,
                    navigateBack = navController::navigateUp,
                    navigateToShareUUID = { navController.navigate(Routes.SHARE_UUID) }
                )
            }
            composable(Routes.SHARE_UUID) { //TODO : figure out how to tag this route to differentiate between this and ImportPatientCaseLayout when passing a Patient
                ShareUUIDLayout(
                    navigateBack = navController::navigateUp,
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(Routes.CHOOSE_LOGIN) {
                ChooseProfileLayout(
                    modifier = Modifier.fillMaxSize(),
                    navigateToPatientLogin = { navController.navigate(Routes.PATIENT_LOGIN) },
                    navigateToDoctorLogin = { navController.navigate(Routes.DOCTOR_LOGIN) }
                )
            }
            composable(Routes.PATIENT_LOGIN) {
                PatientLoginLayout { navController.navigate(Routes.CASE_LIST_PATIENT) }
            }
            composable(Routes.DOCTOR_LOGIN) {
                DoctorLoginLayout(
                    loginDataStore = loginDataStore,
                    navigateToCaseList = { navController.navigate(Routes.CASE_LIST_DOCTOR) }
                )
            }
            composable<Patient> { backStackEntry ->
                val patient: Patient = backStackEntry.toRoute()

                ImportPatientCaseLayout(
                    navigateBack = navController::navigateUp,
                    patient = patient,
                    modifier = Modifier.fillMaxSize(),
                    onCaseClicked = { case: Case ->
                        Logger.d("Case: $case", tag = "IMPORT")
                        navController.navigateUp()
                    },
                    //TODO : FETCH Patient's cases
                )
            }

            composable<Case> { backStackEntry ->
                val case: Case = backStackEntry.toRoute()

                CaseLayout(
                    case = case,
                    navigateBack = navController::navigateUp,
                )
            }
        }
    }
}


