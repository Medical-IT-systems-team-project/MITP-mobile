package org.umcs.mobile.composables.shared.new_uuid

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import com.slapps.cupertino.adaptive.Theme
import kotlinx.coroutines.launch
import mobileapp.composeapp.generated.resources.Res
import mobileapp.composeapp.generated.resources.qr_scanner
import mobileapp.composeapp.generated.resources.wrong_uuid
import org.jetbrains.compose.resources.painterResource
import org.umcs.mobile.composables.shared.AdaptiveFAB
import org.umcs.mobile.composables.shared.AppTopBar
import org.umcs.mobile.theme.determineTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewUUIDScreen(
    navigateBack: (() -> Unit)? = null,
    onSuccessButtonClick: suspend (String) -> String?,
    title: String,
    label: String,
) {
    val scope = rememberCoroutineScope()
    var showQrScanner by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }
    var supportingText by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    Scaffold(
        modifier = Modifier.fillMaxSize().clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = focusManager::clearFocus
        ),
        topBar = {
            AppTopBar(
                navigateBack = navigateBack,
                title = title,
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
            )
        },
        floatingActionButton = {
            AdaptiveFAB(
                modifier = Modifier.offset(y = (-40).dp),
                onClick = { showQrScanner = !showQrScanner },
                iconResource = Res.drawable.qr_scanner
            )
        }
    ) { paddingValues ->

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            QrScannerContent(showQrScanner, onQrScannerCompletion = { UUID ->
                text = UUID
                Logger.i(UUID, tag = "UUID")
            })
            if(determineTheme() == Theme.Cupertino){
                Spacer(Modifier.height(80.dp))
            }
            AdaptiveAccessIdInput(
                label = label,
                text = text,
                supportingText = supportingText,
                isFocused = isFocused,
                focusRequester = focusRequester,
                onTextChange = { text = it },
                onFocusChange = { changedFocus -> isFocused = changedFocus },
                onSupportingTextChange = { supportingText = it },
                onButtonPress = {
                    when {
                        text.isEmpty() -> supportingText = "Please enter the access ID"
                        !matchAccessID(text) -> supportingText = "Please a valid  access ID"
                        matchAccessID(text) -> {
                            scope.launch {
                                val errorMessage = onSuccessButtonClick(text)

                                if(errorMessage != null){
                                    supportingText = errorMessage
                                    text = ""
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}


@Composable
fun ErrorIcon(modifier: Modifier = Modifier) {
    Icon(
        modifier = modifier.size(20.dp),
        tint = MaterialTheme.colorScheme.error,
        painter = painterResource(Res.drawable.wrong_uuid),
        contentDescription = null
    )
}

fun matchAccessID(accessID: String): Boolean {
    val regex =
        Regex("^[0-9a-zA-Z]{10}\$")

    return if (regex.matches(accessID)) {
        Logger.i("ID is valid", tag = "UUID")
        true
    } else {
        Logger.i("ID is invalid", tag = "UUID")
        false
    }
}