import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import co.touchlab.kermit.Logger
import com.slapps.cupertino.adaptive.AdaptiveCircularProgressIndicator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.compose.viewmodel.koinViewModel
import org.umcs.mobile.composables.shared.new_uuid.NewUUIDScreen
import org.umcs.mobile.network.AllMedicalCasesResult
import org.umcs.mobile.network.GlobalKtorClient
import org.umcs.mobile.network.PatientLoginResult

@Composable
fun PatientLoginLayout(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = koinViewModel(),
    navigateToCaseList: () -> Unit,
    loginDataStore: DataStore<Preferences>,
) {
    val title = "Login as Patient"
    val label = "Access ID"
    val accessIdKey = stringPreferencesKey("access_id")
    var loading by remember { mutableStateOf(true) }

    suspend fun handleLogin(accessId: String): String? {
        val patientLoginResult = GlobalKtorClient.loginAsPatient(accessId)

        when (patientLoginResult) {
            is PatientLoginResult.Error -> return patientLoginResult.message
            is PatientLoginResult.Success -> {
                val loggedInPatient = patientLoginResult.patient
                viewModel.setPatient(loggedInPatient)

                val getPatientMedicalCasesResult = GlobalKtorClient.getAllMedicalCasesAsPatient(loggedInPatient.accessId)
                when (getPatientMedicalCasesResult) {
                    is AllMedicalCasesResult.Error -> Logger.i(getPatientMedicalCasesResult.message)
                    is AllMedicalCasesResult.Success -> viewModel.setMedicalCases(getPatientMedicalCasesResult.cases)
                }
                loginDataStore.edit { preferences ->
                    preferences[accessIdKey] = accessId
                }
                navigateToCaseList()
                return null
            }
        }
    }

    LaunchedEffect(Unit) {
        val storedAccessId = loginDataStore.data.map { preferences ->
            preferences[accessIdKey]
        }.first()

        storedAccessId?.let { accessId ->
            handleLogin(accessId)
        }
        loading = false
    }

    if (loading) {
        AdaptiveCircularProgressIndicator(modifier = Modifier.fillMaxSize())
    } else {
        NewUUIDScreen(
            onSuccessButtonClick = { passedAccessId ->
                handleLogin(passedAccessId)
            },
            title = title,
            label = label,
        )
    }
}