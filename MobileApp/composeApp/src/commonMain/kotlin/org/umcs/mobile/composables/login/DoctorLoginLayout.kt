package org.umcs.mobile.composables.login

import AppViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import mobileapp.composeapp.generated.resources.Res
import mobileapp.composeapp.generated.resources.caretrack
import mobileapp.composeapp.generated.resources.cross_logo
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.umcs.mobile.composables.shared.AdaptiveTextField
import org.umcs.mobile.network.GlobalKtorClient

@Composable
fun DoctorLoginLayout(
    navigateToCaseList: () -> Unit,
    viewModel: AppViewModel = koinViewModel(),
    loginDataStore: DataStore<Preferences>? = null,
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
            modifier = Modifier.padding(horizontal = 30.dp),
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
            modifier = Modifier.padding(horizontal = 30.dp),
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

        Button(
            onClick = {
                handleLogin(
                    changeLoginError = { newLoginError -> loginError = newLoginError },
                    changePasswordError = { newPasswordError -> passwordError = newPasswordError },
                    changeErrorMessage = { newErrorMessage -> errorMessage = newErrorMessage },
                    login = login,
                    password = password,
                    loginScope = loginScope,
                    navigateToCaseList = navigateToCaseList
                )
            },
            shape = shape,
            modifier = Modifier.width(270.dp)
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

private fun handleLogin(
    changeLoginError: (String) -> Unit,
    changePasswordError: (String) -> Unit,
    changeErrorMessage: (String) -> Unit,
    login: String,
    password: String,
    loginScope: CoroutineScope,
    navigateToCaseList: () -> Unit,
) {
    changeLoginError("")
    changePasswordError("")
    changeErrorMessage("")

    if (login.isNotBlank() && password.isNotBlank()) {
        loginScope.launch {
            val successful = GlobalKtorClient.loginAsDoctor(login, password)
            if (successful) {
                navigateToCaseList()
            } else {
                changeErrorMessage("Something went wrong")
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



