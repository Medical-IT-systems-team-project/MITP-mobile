import androidx.lifecycle.ViewModel
import co.touchlab.kermit.Logger
import org.umcs.mobile.data.Case
import org.umcs.mobile.data.Patient
import org.umcs.mobile.network.dto.case.MedicalCaseResponseDto
import org.umcs.mobile.network.dto.case.toCaseList
import org.umcs.mobile.network.dto.patient.PatientResponseDto
import org.umcs.mobile.network.dto.patient.toPatientList

class AppViewModel : ViewModel() {
    var doctorID = ""
        private set

    val isDoctor: Boolean
        get() = doctorID.isNotBlank()

    lateinit var patient: Patient
        private set

    lateinit var medicalCaseList : List<Case>
        private set

    lateinit var patientList : List<Patient>
        private set

    fun setMedicalCases(cases : List<MedicalCaseResponseDto>){
        medicalCaseList = cases.toCaseList()
        Logger.v(medicalCaseList.toString(), tag = "Finito")
    }

    fun setPatients(patients : List<PatientResponseDto>) {
        patientList = patients.toPatientList()
        Logger.v(patientList.toString(), tag = "Finito")
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

}

