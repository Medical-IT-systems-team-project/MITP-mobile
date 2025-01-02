package org.umcs.mobile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.navigation.NavHostController
import com.slapps.cupertino.adaptive.AdaptiveScaffold
import com.slapps.cupertino.adaptive.AdaptiveTextButton
import com.slapps.cupertino.adaptive.AdaptiveTonalButton
import com.slapps.cupertino.adaptive.ExperimentalAdaptiveApi
import com.slapps.cupertino.adaptive.Theme
import com.slapps.cupertino.theme.CupertinoTheme
import compose.icons.FeatherIcons
import compose.icons.feathericons.Moon
import compose.icons.feathericons.UserMinus
import compose.icons.feathericons.UserPlus
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.umcs.mobile.composables.shared.AdaptiveWheelDateTimePicker
import org.umcs.mobile.navigation.Routes
import org.umcs.mobile.theme.determineTheme

@OptIn(ExperimentalAdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun App(
    navController: NavHostController,
    testDataStore: DataStore<Preferences>,
) {

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

            LaunchedEffect(Unit) {
             /*   try {
                    val test = GlobalKtorClient.testNewCase()
                    Logger.i("$test", tag = "Ktor")
                } catch (e: Exception) {
                    Logger.i("wyjebalo sie", tag = "Ktor")
                }*/
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
            var showThisBitch by remember { mutableStateOf(true) }

            if (showThisBitch) {
                AdaptiveWheelDateTimePicker(
                    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                    dismiss = { showThisBitch = false }
                )
            }

            var text by remember { mutableStateOf("") }

            AdaptiveTonalButton(
              onClick ={},
                modifier = Modifier.fillMaxWidth()
            ){
                Text("Some long ass text")
            }
            /* CupertinoWheelPicker(
                 state = rememberCupertinoPickerState(),
                 items = List(100) { it + 1 },
             ) { item ->
                 Text(text = item.toString())
             }*/
        }
    }
}

