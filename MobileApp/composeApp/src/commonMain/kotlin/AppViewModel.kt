import androidx.lifecycle.ViewModel

class AppViewModel : ViewModel() {
    var doctorID = 1
        private set
    fun setDoctorId(id : Int){
        doctorID = id
    }

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

