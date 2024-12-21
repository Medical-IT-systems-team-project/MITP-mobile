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
import org.umcs.mobile.composables.case_list_view.doctor.ImportNewPatient
import org.umcs.mobile.composables.case_view.CaseLayout
import org.umcs.mobile.composables.import_patient.ImportPatientCaseLayout
import org.umcs.mobile.composables.login.ChooseProfileLayout
import org.umcs.mobile.composables.login.DoctorLoginLayout
import org.umcs.mobile.composables.login.PatientLoginLayout
import org.umcs.mobile.composables.new_case_view.NewCaseLayout
import org.umcs.mobile.composables.new_medication_view.NewMedicationLayout
import org.umcs.mobile.composables.new_patient_view.NewPatientLayout
import org.umcs.mobile.composables.new_treatment_view.NewTreatmentLayout
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
            /*
                        TestAdaptive()
            */
        }
        composable<Routes.CaseListDoctor> {
            CaseListLayout(
                isDoctor = true,
                navigateToImportNewPatient = {navController.navigate(Routes.ImportPatient)},
                navigateToCase = { case -> navController.navigate(Routes.CaseDetailsDoctor(case)) },
                navigateBack = navController::navigateUp,
                navigateToAddNewPatient = { navController.navigate(Routes.NewPatient) },
                navigateToAddNewCase = { navController.navigate(Routes.NewCase) },
                navigateToImportPatientCase = { patient ->
                    navController.navigate(
                        Routes.ImportCase(
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
        composable<Routes.NewTreatment> {backStackEntry ->
            val caseID = backStackEntry.toRoute<Routes.NewTreatment>().medicalCaseID

            NewTreatmentLayout(
                navigateBack = navController::navigateUp,
                medicalCaseID = caseID
            )
        }
        composable<Routes.NewMedication> { backStackEntry ->
            val caseID = backStackEntry.toRoute<Routes.NewMedication>().medicalCaseID

            NewMedicationLayout(
                navigateBack = navController::navigateUp,
                medicalCaseID = caseID
            )
        }
        composable<Routes.CaseListPatient> {
            CaseListLayout(
                isDoctor = false,
                navigateToCase = { case -> navController.navigate(Routes.CaseDetailsPatient(case)) },
                navigateBack = navController::navigateUp,
                navigateToSharePatientUUID = { patient ->
                    navController.navigate(Routes.ShareUUID(patient))
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
        composable<Routes.ImportPatient>{
            ImportNewPatient(
                navigateBack = navController::navigateUp,
                onSuccess = {}
            )
        }
        composable<Routes.DoctorLogin> {
            DoctorLoginLayout(
                loginDataStore = loginDataStore,
                navigateToCaseList = { navController.navigate(Routes.CaseListDoctor) }
            )
        }
        composable<Routes.ImportCase>(
            typeMap = mapOf(
                typeOf<Patient>() to CustomNavType.PatientType
            )
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<Routes.ImportCase>()

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
            val patient = route.patient

            ShareUUIDLayout(
                navigateBack = navController::navigateUp,
                modifier = Modifier.fillMaxSize(),
                patientName = patient.firstName,
            )
        }

        composable<Routes.CaseDetailsDoctor>(
            typeMap = mapOf(
                typeOf<Case>() to CustomNavType.CaseType
            )
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<Routes.CaseDetailsDoctor>()

            CaseLayout(
                isDoctor = true,
                case = route.case,
                navigateBack = navController::navigateUp,
                navigateToNewTreatment = { medicalCaseID : Int -> navController.navigate(Routes.NewTreatment(medicalCaseID)) },
                navigateToNewMedication = { medicalCaseID : Int -> navController.navigate(Routes.NewMedication(medicalCaseID)) }
            )
        }

        composable<Routes.CaseDetailsPatient>(
            typeMap = mapOf(
                typeOf<Case>() to CustomNavType.CaseType
            )
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<Routes.CaseDetailsDoctor>()

            CaseLayout(
                isDoctor = false,
                case = route.case,
                navigateBack = navController::navigateUp,
                navigateToNewTreatment = { medicalCaseID : Int -> navController.navigate(Routes.NewTreatment(medicalCaseID)) },
                navigateToNewMedication = { medicalCaseID : Int -> navController.navigate(Routes.NewMedication(medicalCaseID)) }
            )
        }
    }
}

