package org.umcs.mobile.composables.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mobileapp.composeapp.generated.resources.Res
import mobileapp.composeapp.generated.resources.caduceus
import mobileapp.composeapp.generated.resources.cross_logo
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun ChooseProfileButton(onClick: () -> Unit, drawableResource: DrawableResource, bottomText: String) {
    FloatingActionButton(
        contentColor = MaterialTheme.colorScheme.primary,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth(0.7f).aspectRatio(1f),
        onClick = onClick,
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Icon(
                modifier = Modifier.fillMaxSize(0.8f),
                painter = painterResource(drawableResource),
                contentDescription = bottomText
            )
            Text(
                fontSize = 19.sp,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
                text = bottomText,
                modifier = Modifier.fillMaxWidth(0.8f)
            )
        }
    }
}

@Composable
fun ChooseProfileLayout(
    modifier: Modifier = Modifier,
    navigateToPatientLogin: () -> Unit,
    navigateToDoctorLogin: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(50.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ChooseProfileButton(
            onClick = navigateToPatientLogin,
            drawableResource = Res.drawable.cross_logo,
            bottomText = "Patient"
        )
        ChooseProfileButton(
            onClick = navigateToDoctorLogin,
            drawableResource = Res.drawable.caduceus,
            bottomText = "Doctor"
        )
    }
}
