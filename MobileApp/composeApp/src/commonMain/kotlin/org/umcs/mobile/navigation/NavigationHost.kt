package org.umcs.mobile.navigation

import PatientLoginLayout
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
    doctorLoginDataStore: DataStore<Preferences>,
    patientLoginDataStore: DataStore<Preferences>,
) {

    NavHost(
        navController = navController,
        modifier = Modifier.fillMaxSize(),
        startDestination = Routes.ChooseLogin
    ) {
        composable<Routes.Home> {
            App(
                navController = navController,
                testDataStore = patientLoginDataStore
            )
            /*
                        TestAdaptive()
            */
        }
        composable<Routes.CaseListDoctor> {
            CaseListLayout(
                isDoctor = true,
                navigateToImportNewPatient = { navController.navigate(Routes.ImportPatient) },
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
        composable<Routes.NewTreatment> { backStackEntry ->
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
            PatientLoginLayout(
                loginDataStore = patientLoginDataStore,
                navigateToCaseList = {
                    navController.navigate(Routes.CaseListPatient) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<Routes.ImportPatient> {
            ImportNewPatient(
                navigateBack = navController::navigateUp,
            )
        }
        composable<Routes.DoctorLogin> {
            DoctorLoginLayout(
                loginDataStore = doctorLoginDataStore,
                navigateToCaseList = {
                    navController.navigate(Routes.CaseListDoctor) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                }
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
                patientAccessID = patient.accessID
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
                caseId = route.case.id,
                navigateBack = navController::navigateUp,
                navigateToNewTreatment = { medicalCaseID: Int ->
                    navController.navigate(
                        Routes.NewTreatment(
                            medicalCaseID
                        )
                    )
                },
                navigateToNewMedication = { medicalCaseID: Int ->
                    navController.navigate(
                        Routes.NewMedication(
                            medicalCaseID
                        )
                    )
                }
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
                caseId = route.case.id,
                navigateBack = navController::navigateUp,
                navigateToNewTreatment = { medicalCaseID: Int ->
                    navController.navigate(
                        Routes.NewTreatment(
                            medicalCaseID
                        )
                    )
                },
                navigateToNewMedication = { medicalCaseID: Int ->
                    navController.navigate(
                        Routes.NewMedication(
                            medicalCaseID
                        )
                    )
                }
            )
        }
    }
}

