package org.umcs.mobile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.navigation.NavHostController
import co.touchlab.kermit.Logger
import compose.icons.FeatherIcons
import compose.icons.feathericons.Moon
import compose.icons.feathericons.UserMinus
import compose.icons.feathericons.UserPlus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.umcs.mobile.navigation.Routes
import org.umcs.mobile.network.GlobalKtorClient

@Composable
internal fun App(
    navController: NavHostController,
    testDataStore: DataStore<Preferences>
) {
    //!TODO : Add Doctor CaseListView - DONE
    //!TODO : Add Patient CaseListView  - DONE
    //!TODO : Add Patient sharing QR UUID - DONE
    //!TODO : Starting View (either doctor or patient) - DONE

    //TODO : Case Info View - DONE
    //TODO : Case Treatment View
    //TODO : Case Medicine View
    //TODO : Modify Case View
    //TODO : Hold user data (either patient or doctor)
    //TODO : Handle API requests (patient should log in with uuid, doctor should log in with username and password)
    //TODO : Refactor KtorClient to not be platform specific (didnt know you could just init it without specifics and the platform related stuff would happen by itself)

    val scope = rememberCoroutineScope()
    val counterKey = intPreferencesKey("counter")
    val counter by testDataStore
        .data
        .map { it[counterKey] ?: 0 }
        .collectAsState(0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedButton(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp).widthIn(min = 200.dp),
            onClick = { navController.navigate(Routes.CaseListDoctor) },
            content = {
                Icon(imageVector = FeatherIcons.Moon, contentDescription = null)
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(text = "Case list as a doctor")
            }
        )

        ElevatedButton(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp).widthIn(min = 200.dp),
            onClick = { navController.navigate(Routes.CaseListPatient) },
            content = {
                Icon(imageVector = FeatherIcons.Moon, contentDescription = null)
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(text = "Case list as a patient")
            }
        )

        ElevatedButton(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp).widthIn(min = 200.dp),
            onClick = { navController.navigate(Routes.PatientLogin) },
            content = {
                Icon(imageVector = FeatherIcons.UserPlus, contentDescription = null)
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(text = "Log in as Patient")
            }
        )

        ElevatedButton(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp).widthIn(min = 200.dp),
            onClick = { navController.navigate(Routes.DoctorLogin) },
            content = {
                Icon(imageVector = FeatherIcons.UserMinus, contentDescription = null)
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(text = "Log in as Doctor")
            }
        )

        ElevatedButton(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp).widthIn(min = 200.dp),
            onClick = { navController.navigate(Routes.ChooseLogin) },
            content = {
                Icon(imageVector = FeatherIcons.UserMinus, contentDescription = null)
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(text = "Choose login")
            }
        )

        FloatingActionButton(
           onClick = {
               scope.launch {
                    testDataStore.edit {
                        it[counterKey] = counter + 1
                    }
               }
            }
        ){
            Text(text = counter.toString(), fontSize = 21.sp)
        }

        LaunchedEffect(Unit){
            delay(1000)
            try {
                val randomJson = GlobalKtorClient.getRandomJson()
                Logger.i("Random json: $randomJson",tag ="JSON")
            } catch (e: Exception) {
                Logger.e(tag = "JSON", throwable = e, { "wyjebalo sie $e" })
            }
        }

    }
}
