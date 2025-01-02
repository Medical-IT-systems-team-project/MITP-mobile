package org.umcs.mobile.composables.share_uuid_view

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.slapps.cupertino.adaptive.Theme
import com.slapps.cupertino.theme.CupertinoTheme
import io.github.alexzhirkevich.qrose.options.Neighbors
import io.github.alexzhirkevich.qrose.options.QrLogo
import io.github.alexzhirkevich.qrose.options.QrLogoPadding
import io.github.alexzhirkevich.qrose.options.QrLogoShape
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import mobileapp.composeapp.generated.resources.Res
import mobileapp.composeapp.generated.resources.cross_logo
import org.jetbrains.compose.resources.painterResource
import org.umcs.mobile.composables.shared.AppTopBar
import org.umcs.mobile.theme.backgroundLight
import org.umcs.mobile.theme.determineTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareUUIDLayout(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    patientAccessID: String = generateRandomNumber(),
    patientName: String = ""
) {
    val style = when(determineTheme()){
        Theme.Cupertino -> CupertinoTheme.typography.title3
        Theme.Material3 -> MaterialTheme.typography.titleMedium
    }
    val title = if(patientName.isNotBlank()) "$patientName's Access ID" else "Access ID"

    Scaffold(
        modifier = modifier,
        topBar = { AppTopBar(
            navigateBack = navigateBack,
            title = title,
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        ) },
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
                    .fillMaxWidth(0.87f)
                    .aspectRatio(1f)
                    .background(backgroundLight)
                    .padding(10.dp),
                painter = rememberQrCodePainter(
                    data = patientAccessID,
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
                    text = patientAccessID,
                    maxLines = 1,
                    style = style,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

fun generateRandomNumber(length: Int = 10): String {
    val digits = "0123456789"
    return (1..length)
        .map { digits.random() }
        .joinToString("")
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