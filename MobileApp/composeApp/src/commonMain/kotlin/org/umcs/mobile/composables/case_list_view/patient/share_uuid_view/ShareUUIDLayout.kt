package org.umcs.mobile.composables.case_list_view.patient.share_uuid_view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.alexzhirkevich.qrose.options.Neighbors
import io.github.alexzhirkevich.qrose.options.QrLogo
import io.github.alexzhirkevich.qrose.options.QrLogoPadding
import io.github.alexzhirkevich.qrose.options.QrLogoShape
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import mobileapp.composeapp.generated.resources.Res
import mobileapp.composeapp.generated.resources.cross_logo
import org.jetbrains.compose.resources.painterResource
import org.umcs.mobile.theme.backgroundLight
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
fun ShareUUIDLayout(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    patientUUID: String = Uuid.random().toString()
) {
    Scaffold(
        modifier = modifier,
        topBar = { ShareUUIDTopBar(navigateBack = navigateBack) },
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(40.dp, Alignment.Top),
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Image(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .aspectRatio(1f)
                    .background(backgroundLight),
                painter = rememberQrCodePainter(
                    data = patientUUID,
                    logo = QrLogo(
                        shape = CrossQrLogoShape(),
                        padding = QrLogoPadding.Accurate(-0.3f),
                        painter = painterResource(Res.drawable.cross_logo),
                    ),
                ),
                contentDescription = "QR code with patient identifier"
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = patientUUID,
                    maxLines = 1,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

class CrossQrLogoShape : QrLogoShape {
    override fun Path.path(size: Float, neighbors: Neighbors): Path {
        val thickness = size * 0.3f
        val halfThickness = thickness / 2

        // Vertical part of cross
        moveTo(size / 2 - halfThickness, 0f)
        lineTo(size / 2 + halfThickness, 0f)
        lineTo(size / 2 + halfThickness, size)
        lineTo(size / 2 - halfThickness, size)
        close()

        // Horizontal part of cross
        moveTo(0f, size / 2 - halfThickness)
        lineTo(size, size / 2 - halfThickness)
        lineTo(size, size / 2 + halfThickness)
        lineTo(0f, size / 2 + halfThickness)
        close()

        return this
    }
}