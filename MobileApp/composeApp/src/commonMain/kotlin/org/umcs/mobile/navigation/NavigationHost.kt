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
import kotlin.reflect.typeOf


@Composable
fun NavigationHost(
    navController: NavHostController = rememberNavController(),
    loginDataStore: DataStore<Preferences>,
    testDataStore: DataStore<Preferences>,
) {

    NavHost(
        navController = navController,
        modifier = Modifier.fillMaxSize(),
        startDestination = Routes.Home
    ) {
        composable<Routes.Home> {
            App(
                navController = navController,
                testDataStore = testDataStore
            )
        }
        composable<Routes.CaseListDoctor> {
            CaseListLayout(
                isDoctor = true,
                navigateToCase = { case -> navController.navigate(Routes.CaseDetails(case)) },
                navigateBack = navController::navigateUp,
                navigateToAddNewPatient = { navController.navigate(Routes.NewPatient) },
                navigateToAddNewCase = { navController.navigate(Routes.NewCase) },
                navigateToImportPatientCase = { patient ->
                    navController.navigate(
                        Routes.ImportPatient(
                            patient
                        )
                    )
                },
                navigateToSharePatientUUID = { patient ->
                    navController.navigate(
                        Routes.ShareUUID(
                            patient
                        )
                    )
                }
            )
        }
        composable<Routes.NewPatient> {
            NewPatientLayout(
                navigateBack = navController::navigateUp
            )
        }
        composable<Routes.NewCase> {
            NewCaseLayout(
                navigateBack = navController::navigateUp
            )
        }
        composable<Routes.CaseListPatient> {
            CaseListLayout(
                isDoctor = false,
                navigateToCase = { case -> navController.navigate(Routes.CaseDetails(case)) },
                navigateBack = navController::navigateUp,
                navigateToSharePatientUUID = { patient ->
                    navController.navigate(
                        Routes.ShareUUID(
                            patient
                        )
                    )
                }
            )
        }
        composable<Routes.ChooseLogin> {
            ChooseProfileLayout(
                modifier = Modifier.fillMaxSize(),
                navigateToPatientLogin = { navController.navigate(Routes.PatientLogin) },
                navigateToDoctorLogin = { navController.navigate(Routes.DoctorLogin) }
            )
        }
        composable<Routes.PatientLogin> {
            PatientLoginLayout { navController.navigate(Routes.CaseListPatient) }
        }
        composable<Routes.DoctorLogin> {
            DoctorLoginLayout(
                loginDataStore = loginDataStore,
                navigateToCaseList = { navController.navigate(Routes.CaseListDoctor) }
            )
        }
        composable<Routes.ImportPatient>(
            typeMap = mapOf(
                typeOf<Patient>() to CustomNavType.PatientType
            )
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<Routes.ImportPatient>()

            ImportPatientCaseLayout(
                navigateBack = navController::navigateUp,
                patient = route.patient,
                modifier = Modifier.fillMaxSize(),
                onCaseClicked = { case ->
                    Logger.d("Case: $case", tag = "IMPORT")
                    navController.navigateUp()
                }
            )
        }
        composable<Routes.ShareUUID>(
            typeMap = mapOf(
                typeOf<Patient>() to CustomNavType.PatientType
            )
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<Routes.ShareUUID>()

            ShareUUIDLayout(
                navigateBack = navController::navigateUp,
                modifier = Modifier.fillMaxSize()
            )
        }
        composable<Routes.CaseDetails>(
            typeMap = mapOf(
                typeOf<Case>() to CustomNavType.CaseType
            )
        ){ backStackEntry ->
            val route = backStackEntry.toRoute<Routes.CaseDetails>()

            CaseLayout(
                case = route.case,
                navigateBack = navController::navigateUp
            )
        }
    }
}

