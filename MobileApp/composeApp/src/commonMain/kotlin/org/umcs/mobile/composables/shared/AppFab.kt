package org.umcs.mobile.composables.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.slapps.cupertino.CupertinoIconButton
import com.slapps.cupertino.adaptive.AdaptiveWidget
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.umcs.mobile.theme.onSurfaceVariantDark

@Composable
fun AppFab(
    modifier: Modifier = Modifier,
    iconResource: DrawableResource? = null,
    iconVector: ImageVector? = null,
    onClick: () -> Unit,
) {
    FloatingActionButton(
        modifier = modifier.size(60.dp),
        onClick = onClick,
    ) {
        if (iconVector != null) {
            Icon(
                imageVector = iconVector,
                contentDescription = null
            )
        } else {
            Icon(
                painter = painterResource(iconResource!!),
                contentDescription = null
            )
        }
    }
}

@Composable
fun AdaptiveFAB(
    modifier: Modifier = Modifier,
    iconResource: DrawableResource? = null,
    iconVector: ImageVector? = null,
    onClick: () -> Unit,
) {
    val cupertinoContainer = MaterialTheme.colorScheme.primaryContainer
    val cupertinoContent = onSurfaceVariantDark
    val customElevation = FloatingActionButtonDefaults.elevation(
        defaultElevation = 12.dp,
        pressedElevation = 12.dp,
        focusedElevation = 6.dp,
        hoveredElevation = 10.dp
    )

    AdaptiveWidget(
        material = {
            AppFab(
                modifier = modifier,
                iconResource = iconResource,
                iconVector = iconVector,
                onClick = onClick
            )
        },
        cupertino = {
            CupertinoIconButton(
               // shape = CircleShape,
               // elevation = customElevation,
               /* modifier = Modifier.background(
                    Brush.linearGradient(
                        colors = listOf(cupertinoContainer, cupertinoContainer.copy(alpha = 0.6f))
                    ),
                    CircleShape
                ),*/
              //  containerColor = Color.Transparent,
               // contentColor = cupertinoContent.copy(alpha = 0.7f),
                modifier = modifier.size(60.dp).clip(CircleShape).background(cupertinoContainer),
                onClick = onClick,
                content = {
                    if (iconVector != null) {
                        Icon(
                            tint = cupertinoContent,
                            imageVector = iconVector,
                            contentDescription = null
                        )
                    } else {
                        Icon(
                            tint = cupertinoContent,
                            painter = painterResource(iconResource!!),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    )
}
