import androidx.lifecycle.ViewModel
import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.umcs.mobile.composables.case_view.Medication
import org.umcs.mobile.composables.case_view.Treatment
import org.umcs.mobile.data.Case
import org.umcs.mobile.data.Patient
import org.umcs.mobile.network.dto.case.MedicalCaseResponseDto
import org.umcs.mobile.network.dto.case.MedicalStatus
import org.umcs.mobile.network.dto.case.toCaseList
import org.umcs.mobile.network.dto.patient.PatientResponseDto
import org.umcs.mobile.network.dto.patient.toPatientList

class AppViewModel : ViewModel() {
    private val updateScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    var doctorID = ""
        private set

    val isDoctor: Boolean
        get() = doctorID.isNotBlank()

    var patient: Patient = Patient.emptyPatient()
        private set

    private val _medicalCaseList = MutableStateFlow<List<Case>>(emptyList())
    val medicalCaseList: StateFlow<List<Case>> = _medicalCaseList

    private val _patientList = MutableStateFlow<List<Patient>>(emptyList())
    val patientList: StateFlow<List<Patient>> = _patientList

    private val _unassignedPatientList = MutableStateFlow<List<Patient>>(emptyList())
    val unassignedPatientList : StateFlow<List<Patient>> = _unassignedPatientList

    fun setMedicalCases(cases: List<MedicalCaseResponseDto>) {
        _medicalCaseList.value = cases.toCaseList()
        Logger.v(cases.toString(), tag = "CaseList")
        Logger.v(_medicalCaseList.value.toString(), tag = "CaseList")
    }

    fun areAnyTreatmentsOrMedicationsCompletedOrCancelled(caseId: Int): Boolean {
        val case = _medicalCaseList.value.find { it.id == caseId } ?: return false

        val anyTreatmentCompletedOrCancelled = case.treatments.any {
            it.status != MedicalStatus.COMPLETED || it.status != MedicalStatus.CANCELLED
        }
        val anyMedicationCompletedOrCancelled = case.medications.any {
            it.status != MedicalStatus.COMPLETED || it.status != MedicalStatus.CANCELLED
        }

        return anyTreatmentCompletedOrCancelled || anyMedicationCompletedOrCancelled
    }

    fun setUnassignedPatients(patients : List<PatientResponseDto>){
        _unassignedPatientList.value = patients.toPatientList()
    }

    fun setPatients(patients: List<PatientResponseDto>) {
        _patientList.value = patients.toPatientList()
        Logger.v(_patientList.value.toString(), tag = "Finito")
    }

    fun getCaseById(caseId: Int): Case {
        return _medicalCaseList.value.find { it.id == caseId }!!
    }

    fun setPatient(fetchedPatient: PatientResponseDto) {
        patient = Patient(
            socialSecurityNumber = fetchedPatient.socialSecurityNumber,
            firstName = fetchedPatient.firstName,
            lastName = fetchedPatient.lastName,
            age = fetchedPatient.age,
            gender = fetchedPatient.gender,
            address = fetchedPatient.address,
            phoneNumber = fetchedPatient.phoneNumber,
            email = fetchedPatient.email,
            birthDate = fetchedPatient.birthDate.toString(),
            accessID = fetchedPatient.accessId
        )
    }

    fun setDoctorId(id: String) {
        doctorID = id
    }

    fun changeMedicationStatus(chosenStatus : MedicalStatus, medication : Medication){
        Logger.i("CHANGING MEDICATION STATUS ", tag = "FINITO")

        updateScope.launch { // FOR SOME REASON VIEWMODEL SCOPE DIDNT WORK HERE ???????
            _medicalCaseList.update { cases ->
                cases.map { case ->
                    val medicationIndex = case.medications.indexOfFirst { it.id == medication.id }
                    if (medicationIndex != -1) {
                        val updatedMedications = case.medications.toMutableList()
                        updatedMedications[medicationIndex] = medication.copy(status = chosenStatus)
                        case.copy(medications = updatedMedications)
                    } else {
                        case
                    }
                }
            }
        }
    }

    fun changeTreatmentStatus(chosenStatus : MedicalStatus, treatment : Treatment){
        Logger.i("CHANGING TREATMENT STATUS ", tag = "FINITO")

        updateScope.launch {
            _medicalCaseList.update { cases ->
                cases.map { case ->
                    val treatmentIndex = case.treatments.indexOfFirst { it.id == treatment.id }
                    if (treatmentIndex != -1) {
                        val updatedTreatments = case.treatments.toMutableList()
                        updatedTreatments[treatmentIndex] = treatment.copy(status = chosenStatus)
                        case.copy(treatments = updatedTreatments)
                    } else {
                        case
                    }
                }
            }
        }
    }

}

