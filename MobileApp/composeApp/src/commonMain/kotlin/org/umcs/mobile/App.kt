package org.umcs.mobile

import androidx.collection.intListOf
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.slapps.cupertino.CupertinoDateTimePicker
import com.slapps.cupertino.CupertinoTimePicker
import com.slapps.cupertino.CupertinoWheelPicker
import com.slapps.cupertino.adaptive.AdaptiveScaffold
import com.slapps.cupertino.adaptive.AdaptiveTextButton
import com.slapps.cupertino.adaptive.ExperimentalAdaptiveApi
import com.slapps.cupertino.adaptive.Theme
import com.slapps.cupertino.rememberCupertinoDateTimePickerState
import com.slapps.cupertino.rememberCupertinoPickerState
import com.slapps.cupertino.rememberCupertinoTimePickerState
import com.slapps.cupertino.theme.CupertinoTheme
import compose.icons.FeatherIcons
import compose.icons.feathericons.Moon
import compose.icons.feathericons.UserMinus
import compose.icons.feathericons.UserPlus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.umcs.mobile.navigation.Routes
import org.umcs.mobile.network.GlobalKtorClient
import org.umcs.mobile.theme.determineTheme

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
internal fun App(
    navController: NavHostController,
    testDataStore: DataStore<Preferences>,
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

    AdaptiveScaffold(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ElevatedButton(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    .widthIn(min = 200.dp),
                onClick = { navController.navigate(Routes.CaseListDoctor) },
                content = {
                    Icon(imageVector = FeatherIcons.Moon, contentDescription = null)
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(text = "Case list as a doctor")
                }
            )

            ElevatedButton(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    .widthIn(min = 200.dp),
                onClick = { navController.navigate(Routes.CaseListPatient) },
                content = {
                    Icon(imageVector = FeatherIcons.Moon, contentDescription = null)
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(text = "Case list as a patient")
                }
            )

            ElevatedButton(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    .widthIn(min = 200.dp),
                onClick = { navController.navigate(Routes.PatientLogin) },
                content = {
                    Icon(imageVector = FeatherIcons.UserPlus, contentDescription = null)
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(text = "Log in as Patient")
                }
            )

            LaunchedEffect(Unit){
                try {
                    val test = GlobalKtorClient.getRandomJson()
                    Logger.i("$test",tag = "Ktor")
                }catch(e : Exception){
                    Logger.i("wyjebalo sie", tag= "Ktor")
                }
            }

            ElevatedButton(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    .widthIn(min = 200.dp),
                onClick = { navController.navigate(Routes.DoctorLogin) },
                content = {
                    Icon(imageVector = FeatherIcons.UserMinus, contentDescription = null)
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(text = "Log in as Doctor")
                }
            )

            ElevatedButton(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    .widthIn(min = 200.dp),
                onClick = { navController.navigate(Routes.ChooseLogin) },
                content = {
                    Icon(imageVector = FeatherIcons.UserMinus, contentDescription = null)
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(text = "Choose login")
                }
            )

            val theme = determineTheme()
            val color = when (theme) {
                Theme.Cupertino -> CupertinoTheme.colorScheme.accent
                Theme.Material3 -> MaterialTheme.colorScheme.primary
            }

            AdaptiveTextButton(
                onClick = {
                    scope.launch {
                        testDataStore.edit {
                            it[counterKey] = counter + 1
                        }
                    }
                }
            ) {
                Text(
                    text = counter.toString(), fontSize = 21.sp,
                )
            }

            CupertinoDateTimePicker(
                state = rememberCupertinoDateTimePickerState(is24Hour = true) //TODO: Implement Date Time Picker from https://github.com/Chaintech-Network/compose_multiplatform_date_time_picker
            )


            /* CupertinoWheelPicker(
                 state = rememberCupertinoPickerState(),
                 items = List(100) { it + 1 },
             ) { item ->
                 Text(text = item.toString())
             }*/
        }
    }
}
