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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.alexzhirkevich.qrose.options.QrLogo
import io.github.alexzhirkevich.qrose.options.QrLogoPadding
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
            verticalArrangement = Arrangement.spacedBy(60.dp, Alignment.Top),
            modifier = Modifier.padding(paddingValues).fillMaxSize(),
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                modifier = Modifier.fillMaxWidth(0.9f).aspectRatio(1f).background(backgroundLight),
                painter = rememberQrCodePainter(
                    data = patientUUID,
                    logo = QrLogo(
                        padding = QrLogoPadding.Accurate(0.002f),
                        painter = painterResource(Res.drawable.cross_logo),
                    ),
                ),
                contentDescription = "QR code referring to the example.com website"
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = patientUUID,
                    maxLines = 1,
                    fontSize = 19.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}