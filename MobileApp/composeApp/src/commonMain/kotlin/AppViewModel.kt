import androidx.lifecycle.ViewModel
import org.umcs.mobile.data.Patient
import org.umcs.mobile.network.dto.patient.PatientResponseDto

class AppViewModel : ViewModel() {
    var doctorID = ""
        private set

    val isDoctor: Boolean
        get() = doctorID.isNotBlank()

    lateinit var patient: Patient
        private set

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

