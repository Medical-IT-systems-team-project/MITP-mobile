package org.umcs.mobile.composables.login

import AppViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import co.touchlab.kermit.Logger
import com.slapps.cupertino.CupertinoButtonDefaults
import com.slapps.cupertino.adaptive.AdaptiveCircularProgressIndicator
import com.slapps.cupertino.adaptive.AdaptiveTonalButton
import com.slapps.cupertino.adaptive.ExperimentalAdaptiveApi
import com.slapps.cupertino.adaptive.Theme
import com.slapps.cupertino.adaptive.icons.AdaptiveIcons
import com.slapps.cupertino.adaptive.icons.Email
import com.slapps.cupertino.adaptive.icons.Lock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mobileapp.composeapp.generated.resources.Res
import mobileapp.composeapp.generated.resources.caretrack
import mobileapp.composeapp.generated.resources.cross_logo
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.umcs.mobile.composables.shared.AdaptiveTextField
import org.umcs.mobile.network.AllMedicalCasesResult
import org.umcs.mobile.network.AllPatientsResult
import org.umcs.mobile.network.DoctorLoginResult
import org.umcs.mobile.network.GlobalKtorClient
import org.umcs.mobile.theme.determineTheme
import org.umcs.mobile.theme.onSurfaceDark

@Serializable
data class LoginData(val login: String, val password: String)


@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun DoctorLoginLayout(
    navigateToCaseList: () -> Unit,
    viewModel: AppViewModel = koinViewModel(),
    loginDataStore: DataStore<Preferences>,
) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val loginScope = rememberCoroutineScope()
    var loginError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    val shape = RoundedCornerShape(16.dp)
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val loginDataKey = stringPreferencesKey("login_data")
    var loading by remember { mutableStateOf(true) }

    val isCupertino = when (determineTheme()) {
        Theme.Cupertino -> true
        Theme.Material3 -> false
    }

    LaunchedEffect(Unit) {
        val storedLoginData = loginDataStore.data.map { preferences ->
            preferences[loginDataKey]
        }.first()

        storedLoginData?.let {
            val loginData = Json.decodeFromString<LoginData>(it)
            handleLogin(
                changeLoginError = { loginError = it },
                changePasswordError = { passwordError = it },
                changeErrorMessage = { errorMessage = it },
                login = loginData.login,
                password = loginData.password,
                loginScope = loginScope,
                loginAsDoctor = { doctorId ->
                    loginScope.launch {
                        viewModel.setDoctorId(doctorId)
                        val getPatientsNetworkCall = GlobalKtorClient.getAllDoctorPatients()
                        val getMedicalCasesNetworkCall =
                            GlobalKtorClient.getAllMedicalCasesAsDoctor()

                        when (getMedicalCasesNetworkCall) {
                            is AllMedicalCasesResult.Error -> Logger.i(
                                "${getMedicalCasesNetworkCall.message}",
                                tag = "Finito"
                            )

                            is AllMedicalCasesResult.Success -> viewModel.setMedicalCases(
                                getMedicalCasesNetworkCall.cases
                            )
                        }
                        when (getPatientsNetworkCall) {
                            is AllPatientsResult.Error -> Logger.i(
                                "${getPatientsNetworkCall.message}",
                                tag = "Finito"
                            )

                            is AllPatientsResult.Success -> viewModel.setPatients(
                                getPatientsNetworkCall.patients
                            )
                        }
                        navigateToCaseList()
                        delay(100)
                        loading = false
                    }
                }
            )
        } ?: run {
            loading = false
        }
    }
    if (loading) {
        Box(Modifier.fillMaxSize()) {
            AdaptiveCircularProgressIndicator(
                modifier = Modifier.fillMaxSize(0.5f).align(Alignment.Center)
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .imePadding()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = focusManager::clearFocus
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .offset(y = (-65).dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .size(150.dp)
                        .offset(y = 50.dp),
                    tint = MaterialTheme.colorScheme.primary,
                    painter = painterResource(Res.drawable.cross_logo),
                    contentDescription = null
                )
                Icon(
                    modifier = Modifier.fillMaxWidth(0.7f),
                    painter = painterResource(Res.drawable.caretrack),
                    contentDescription = null
                )
            }

            AdaptiveTextField(
                leadingIcon = {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = AdaptiveIcons.Outlined.Email,
                        contentDescription = null
                    )
                },
                modifier = Modifier.padding(horizontal = 35.dp).heightIn(min = 45.dp),
                keyboardType = KeyboardType.Email,
                title = { Text("Login") },
                text = login,
                supportingText = loginError,
                focusRequester = focusRequester,
                changeSupportingText = { loginError = it },
                onTextChange = { newLogin ->
                    login = newLogin
                },
                placeholder = { Text("Login") },
            )
            Spacer(Modifier.height(30.dp))

            AdaptiveTextField(
                leadingIcon = {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = AdaptiveIcons.Outlined.Lock,
                        contentDescription = null
                    )
                },
                modifier = Modifier.padding(horizontal = 35.dp).heightIn(min = 45.dp),
                keyboardType = KeyboardType.Password,
                title = { Text("Password") },
                text = password,
                supportingText = passwordError,
                focusRequester = focusRequester,
                changeSupportingText = { passwordError = it },
                onTextChange = { newPassword ->
                    password = newPassword
                },
                placeholder = { Text("Password") },
            )
            Spacer(Modifier.height(30.dp))

            AdaptiveTonalButton(
                onClick = {
                    handleLogin(
                        changeLoginError = { newLoginError -> loginError = newLoginError },
                        changePasswordError = { newPasswordError ->
                            passwordError = newPasswordError
                        },
                        changeErrorMessage = { newErrorMessage -> errorMessage = newErrorMessage },
                        login = login,
                        password = password,
                        loginScope = loginScope,
                        loginAsDoctor = { doctorId ->
                            loginScope.launch {
                                viewModel.setDoctorId(doctorId)
                                val getPatientsNetworkCall = GlobalKtorClient.getAllDoctorPatients()
                                val getMedicalCasesNetworkCall =
                                    GlobalKtorClient.getAllMedicalCasesAsDoctor()

                                when (getMedicalCasesNetworkCall) {
                                    is AllMedicalCasesResult.Error -> Logger.i(
                                        "${getMedicalCasesNetworkCall.message}",
                                        tag = "Finito"
                                    )

                                    is AllMedicalCasesResult.Success -> viewModel.setMedicalCases(
                                        getMedicalCasesNetworkCall.cases
                                    )
                                }
                                when (getPatientsNetworkCall) {
                                    is AllPatientsResult.Error -> Logger.i(
                                        "${getPatientsNetworkCall.message}",
                                        tag = "Finito"
                                    )

                                    is AllPatientsResult.Success -> viewModel.setPatients(
                                        getPatientsNetworkCall.patients
                                    )
                                }
                                loginDataStore.edit { preferences ->
                                    preferences[loginDataKey] =
                                        Json.encodeToString<LoginData>(LoginData(login, password))
                                }
                                navigateToCaseList()
                            }
                        }
                    )
                },
                modifier = Modifier.then(
                    if (isCupertino) Modifier.fillMaxWidth()
                        .padding(horizontal = 35.dp) else Modifier.width(270.dp).height(50.dp)
                ),
                adaptation = {
                    material {
                        colors = ButtonDefaults.filledTonalButtonColors(
                            contentColor = onSurfaceDark,
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    }
                    cupertino {
                        colors = CupertinoButtonDefaults.filledButtonColors(
                            contentColor = onSurfaceDark,
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    }
                }
            ) {
                Text(text = "Login", fontSize = 16.sp)
            }
            Spacer(Modifier.height(30.dp))

            if (errorMessage.isNotBlank()) {
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error, fontSize = 15.sp)
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}

private fun handleLogin(
    changeLoginError: (String) -> Unit,
    changePasswordError: (String) -> Unit,
    changeErrorMessage: (String) -> Unit,
    login: String,
    password: String,
    loginScope: CoroutineScope,
    loginAsDoctor: (String) -> Unit,
) {
    changeLoginError("")
    changePasswordError("")
    changeErrorMessage("")

    if (login.isNotBlank() && password.isNotBlank()) {
        loginScope.launch {
            val networkCallResult = GlobalKtorClient.loginAsDoctor(login, password)

            when (networkCallResult) {
                is DoctorLoginResult.Error -> changeErrorMessage(networkCallResult.message)
                is DoctorLoginResult.Success -> loginAsDoctor(networkCallResult.patient.id)
            }
        }
    } else if (login.isBlank() && password.isBlank()) {
        changeLoginError("This field can't be blank")
        changePasswordError("This field can't be blank")
    } else if (login.isBlank()) {
        changeLoginError("This field can't be blank")
    } else if (password.isBlank()) {
        changePasswordError("This field can't be blank")
    }
}



