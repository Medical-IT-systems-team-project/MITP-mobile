import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import io.ktor.client.call.body
import kotlinx.coroutines.launch
import org.koin.dsl.module
import org.umcs.mobile.network.KtorClient
import org.umcs.mobile.network.Welcome

class AppViewModel : ViewModel() {
    var data by mutableStateOf("poczekaj chwile")

    fun fetchData() {
        try {
            viewModelScope.launch {
                val apiData = KtorClient.get("https://reqres.in/api/users/2")
                val welcome = apiData.body<Welcome>()

                data = welcome.data.toString()
            }
        }catch (e : Exception){
            Logger.e("Something went wrong ${e.message}",e,"AppViewModel")
        }
    }
}

val appModule = module {
    single { AppViewModel() }
}