package org.umcs.mobile.composables.shared.new_uuid

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import co.touchlab.kermit.Logger
import qrscanner.CameraLens
import qrscanner.OverlayShape
import qrscanner.QrScanner

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
