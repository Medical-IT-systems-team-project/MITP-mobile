package org.umcs.mobile.composables.shared

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import co.touchlab.kermit.Logger
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowRight
import mobileapp.composeapp.generated.resources.Res
import mobileapp.composeapp.generated.resources.qr_scanner
import mobileapp.composeapp.generated.resources.wrong_uuid
import org.jetbrains.compose.resources.painterResource
import qrscanner.CameraLens
import qrscanner.OverlayShape
import qrscanner.QrScanner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewUUIDScreen(title : String) {
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
            AppFab(
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

            CaseIdentifierRow(
                text = text,
                supportingText = supportingText,
                isFocused = isFocused,
                focusRequester = focusRequester,
                onTextChange = { text = it },
                onFocusChange = { changedFocus -> isFocused = changedFocus },
                onSupportingTextChange = { supportingText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun QrScannerContent(showQrScanner: Boolean, onQrScannerCompletion: (String) -> Unit) {
    AnimatedVisibility(
        visible = showQrScanner,
        enter = slideInVertically(),
        exit = slideOutVertically()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .aspectRatio(1f)
        ) {
            QrScanner(
                modifier = Modifier.weight(2f),
                flashlightOn = false,
                cameraLens = CameraLens.Back,
                openImagePicker = false,
                onCompletion = onQrScannerCompletion,
                imagePickerHandler = { bool ->
                    Logger.i("Image picker handler", tag = "Image picker handler")
                },
                onFailure = { failure ->
                    Logger.i(failure, tag = "FAILURE")
                },
                overlayShape = OverlayShape.Square,
                overlayColor = MaterialTheme.colorScheme.background,
                overlayBorderColor = MaterialTheme.colorScheme.onBackground
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .zIndex(2f),
            )
        }
    }
}

@Composable
fun CaseIdentifierRow(
    modifier : Modifier = Modifier,
    text: String,
    supportingText: String,
    isFocused: Boolean,
    focusRequester: FocusRequester,
    onTextChange: (String) -> Unit,
    onFocusChange: (Boolean) -> Unit,
    onSupportingTextChange: (String) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            supportingText = {
                Text(
                    supportingText,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(start = 16.dp)
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            value = text,
            onValueChange = onTextChange,
            label = { Text("Case Identifier") },
            enabled = !matchUUID(text),
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester)
                .onFocusChanged {
                    Logger.i(it.isFocused.toString(), tag = "onFocusChanged")
                    onFocusChange(it.isFocused)

                    if (it.isFocused) {
                        onSupportingTextChange("")
                    }
                },
            trailingIcon = {
                if (!matchUUID(text) && text.isNotEmpty() && !isFocused) {
                    ErrorIcon()
                }
            },
            colors = TextFieldDefaults.colors(
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
        )
        FloatingActionButton(
            onClick = {
                when {
                    text.isEmpty() -> onSupportingTextChange("Please enter the identification")
                    !matchUUID(text) -> onSupportingTextChange("Please enter a valid UUID")
                    matchUUID(text) -> {
                        onSupportingTextChange("CORRECT")
                        //TODO : Fetch the user from the backend
                    }
                }
            },
        ) {
            Icon(
                imageVector = FeatherIcons.ArrowRight,
                contentDescription = "Add Case by UUID"
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