package org.umcs.mobile.composables.shared.new_uuid

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import qrscanner.CameraLens
import qrscanner.OverlayShape
import qrscanner.QrScanner

@Composable
fun QrScannerContent(showQrScanner: Boolean, onQrScannerCompletion: (String) -> Unit, modifier: Modifier = Modifier) {
    AnimatedVisibility(
        visible = showQrScanner,
        enter = slideInVertically(),
        exit = slideOutVertically { fullHeight -> fullHeight * -2 }
    ) {
        Column(
            modifier = modifier
        ) {
            QrScanner(
                modifier = Modifier
                    .clipToBounds()
                    .clip(shape = RoundedCornerShape(size = 14.dp))
                    .fillMaxWidth()
                    .aspectRatio(1f)
                ,
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

        }
    }
}
