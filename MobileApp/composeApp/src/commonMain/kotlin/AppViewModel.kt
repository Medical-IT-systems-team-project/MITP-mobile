import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.koin.dsl.module

class AppViewModel : ViewModel() {
    var data by mutableStateOf("poczekaj chwile")

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

val appModule = module {
    single { AppViewModel() }
}