package org.umcs.mobile.composables.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import co.touchlab.kermit.Logger
import qrscanner.CameraLens
import qrscanner.OverlayShape
import qrscanner.QrScanner

@Composable
fun QrCodeScanner(navController: NavHostController) {
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .background(MaterialTheme.colorScheme.background)
                .zIndex(2f),
        )
        QrScanner(
            modifier = Modifier.weight(1f),
            flashlightOn = false,
            cameraLens = CameraLens.Back,
            openImagePicker = false,
            onCompletion = { UUID ->
                text = UUID
                Logger.i(UUID, tag = "UUID")
            },
            imagePickerHandler = { bool ->
                Logger.i("Image picker handler", tag = "Image picker handler")
            },
            onFailure = { failure ->
                Logger.i(failure, tag = "FAILURE")
            },
            overlayShape = OverlayShape.Square,
            overlayColor = MaterialTheme.colorScheme.background
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .zIndex(2f),
            contentAlignment = Alignment.Center
        ) {
            Text(text, fontSize = 30.sp)
        }
    }
}

fun ContentDrawScope.drawCustomOverlay() {
    drawIntoCanvas { canvas ->
        // Example: Draw a red rectangle in the center
        val size = size.minDimension / 2
        val topLeft = center.copy(x = center.x - size / 2, y = center.y - size / 2)
        val bottomRight = center.copy(x = center.x + size / 2, y = center.y + size / 2)
        val rect = Rect(topLeft, bottomRight)

        val paint = Paint().apply {
            color = Color.Red
            style = PaintingStyle.Stroke
        }

        canvas.drawRect(
            rect = rect,
            paint = paint
        )
    }
}
