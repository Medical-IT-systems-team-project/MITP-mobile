package org.umcs.mobile.composables.shared.new_uuid

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import mobileapp.composeapp.generated.resources.Res
import mobileapp.composeapp.generated.resources.qr_scanner
import mobileapp.composeapp.generated.resources.wrong_uuid
import org.jetbrains.compose.resources.painterResource
import org.umcs.mobile.composables.shared.AdaptiveFAB

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewUUIDScreen(
    navigateToCaseList: ()->Unit,
    title : String
) {
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
            CenterAlignedTopAppBar(
                title = { Text(title) }
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

            CaseUUIDInput(
                text = text,
                supportingText = supportingText,
                isFocused = isFocused,
                focusRequester = focusRequester,
                onTextChange = { text = it },
                onFocusChange = { changedFocus -> isFocused = changedFocus },
                onSupportingTextChange = { supportingText = it },
                onButtonPress = navigateToCaseList,
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

fun matchUUID(uuid: String): Boolean {
    val regex =
        Regex("^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}\$")

    return if (regex.matches(uuid)) {
        Logger.i("UUID is valid", tag = "UUID")
        true
    } else {
        Logger.i("UUID is invalid", tag = "UUID")
        false
    }
}