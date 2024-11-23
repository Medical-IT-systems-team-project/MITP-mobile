package org.umcs.mobile.composables.login

import AppViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.viewmodel.compose.viewModel
import mobileapp.composeapp.generated.resources.Res
import mobileapp.composeapp.generated.resources.caretrack
import mobileapp.composeapp.generated.resources.cross_logo
import org.jetbrains.compose.resources.painterResource

@Composable
fun DoctorLoginLayout(
    navigateToCaseList: () -> Unit,
    viewModel: AppViewModel = viewModel { AppViewModel() },
    loginDataStore: DataStore<Preferences>? = null,
) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage = ""
    val shape = RoundedCornerShape(16.dp)
    val colors = TextFieldDefaults.colors(
        unfocusedIndicatorColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent
    )

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(0.8f).offset(y = (-65).dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Icon(
                modifier = Modifier.size(150.dp).offset(y = 50.dp),
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

        TextField(
            modifier = Modifier.fillMaxWidth(0.6f),
            singleLine = true,
            colors = colors,
            placeholder = { Text(text = "Username", fontSize = 16.sp) },
            shape = shape,
            value = login,
            onValueChange = { newLogin ->
                login = newLogin
            }
        )
        Spacer(Modifier.height(30.dp))

        TextField(
            modifier = Modifier.fillMaxWidth(0.6f),
            singleLine = true,
            colors = colors,
            placeholder = { Text(text = "Password", fontSize = 16.sp) },
            shape = shape,
            value = password,
            onValueChange = { newPassword ->
                password = newPassword
            }
        )
        Spacer(Modifier.height(30.dp))

        Button(
            onClick = {
                if (login.isEmpty() || password.isEmpty()) {
                    errorMessage = "ERROR MESSAGE"
                } else {
                    navigateToCaseList()
                }
            },
            shape = shape,
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text(text = "Login", fontSize = 16.sp)
        }
        Spacer(Modifier.height(30.dp))

        errorMessage.let {
            Text(text = it, color = MaterialTheme.colorScheme.error, fontSize = 15.sp)
            Spacer(Modifier.height(20.dp))
        }
    }
}


