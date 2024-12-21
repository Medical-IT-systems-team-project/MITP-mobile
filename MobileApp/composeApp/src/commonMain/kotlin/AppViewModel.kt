import androidx.lifecycle.ViewModel
import org.umcs.mobile.data.Patient
import org.umcs.mobile.network.dto.patient.PatientResponseDto

class AppViewModel : ViewModel() {
    var doctorID = 1
        private set
    lateinit var patient : Patient
        private set

    fun setAccessId(fetchedPatient : PatientResponseDto){ patient = Patient(
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
    ) }
    fun setDoctorId(id : Int){ doctorID = id }

   /* fun fetchData() {
            viewModelScope.launch {
                try {
                    val apiData = KtorClient.get("https://reqres.in/api/users/2")
                    val welcome = apiData.body<Welcome>()

                    data = welcome.data.toString()
                } catch (e: Exception) {
                    data = e.message.toString()
                }
            }
    }*/
}

