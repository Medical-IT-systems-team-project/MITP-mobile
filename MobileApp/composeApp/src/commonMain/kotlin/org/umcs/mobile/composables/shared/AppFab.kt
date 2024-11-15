package org.umcs.mobile.composables.shared

import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun AppFab(
    modifier : Modifier = Modifier,
    iconResource: DrawableResource,
    onClick: () -> Unit
) {

    FloatingActionButton(
        modifier = modifier.size(50.dp),
        onClick = onClick,
    ) {
        Icon(
            modifier= Modifier.size(27.dp),
            painter = painterResource(iconResource),
            contentDescription = null
        )
    }
}