@file:OptIn(ExperimentalAdaptiveApi::class)

package org.umcs.mobile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.slapps.cupertino.adaptive.AdaptiveCheckbox
import com.slapps.cupertino.adaptive.AdaptiveFilledIconButton
import com.slapps.cupertino.adaptive.AdaptiveNavigationBar
import com.slapps.cupertino.adaptive.AdaptiveNavigationBarItem
import com.slapps.cupertino.adaptive.AdaptiveScaffold
import com.slapps.cupertino.adaptive.AdaptiveSurface
import com.slapps.cupertino.adaptive.AdaptiveTopAppBar
import com.slapps.cupertino.adaptive.AdaptiveVerticalDivider
import com.slapps.cupertino.adaptive.AdaptiveWidget
import com.slapps.cupertino.adaptive.ExperimentalAdaptiveApi
import com.slapps.cupertino.adaptive.icons.AdaptiveIcons
import com.slapps.cupertino.adaptive.icons.Home
import com.slapps.cupertino.adaptive.icons.KeyboardArrowLeft
import com.slapps.cupertino.theme.CupertinoTheme
import org.umcs.mobile.theme.AppleTypography

@Composable
fun TestAdaptive() {
    var checkboxState by remember{mutableStateOf(false)}

    AdaptiveSurface(
        modifier = Modifier.fillMaxSize(),
    ) {
        AdaptiveScaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { MyAdaptiveTopBar() },
            bottomBar = { MyAdaptiveNavigationBar() },
            floatingActionButton = { MyAdaptiveFAB() }
        ) {
            AdaptiveCheckbox(
                checked = checkboxState,
                onCheckedChange = {checkboxState = !checkboxState}
            )
        }
    }
}

@Composable
fun MyAdaptiveFAB(icon: ImageVector = AdaptiveIcons.Outlined.Home) {
    val cupertinoBackground = CupertinoTheme.colorScheme.systemGroupedBackground
    val cupertinoContainer = CupertinoTheme.colorScheme.accent
    val cupertinoContent = CupertinoTheme.colorScheme.label
    val customElevation = FloatingActionButtonDefaults.elevation(
        defaultElevation = 8.dp,
        pressedElevation = 12.dp,
        focusedElevation = 6.dp,
        hoveredElevation = 10.dp
    )

    AdaptiveWidget(
        material = {
            FloatingActionButton(
                onClick = {},
                content = { Icon(icon, null) }
            )
        },
        cupertino = {
            FloatingActionButton(
                shape = CircleShape,
                elevation = customElevation,
                modifier = Modifier.background(
                    Brush.linearGradient(
                        colors = listOf(cupertinoContainer, cupertinoBackground)
                    ),
                    CircleShape
                ),
                containerColor = Color.Transparent,
                contentColor = cupertinoContent,
                onClick = {},
                content = { Icon(icon, null) }
            )
        }
    )
}

@Composable
fun MyAdaptiveTopBar() {
    AdaptiveTopAppBar(
        title = { Text(text = "title") },
        navigationIcon = {
            Icon(
                imageVector = AdaptiveIcons.Outlined.KeyboardArrowLeft,
                contentDescription = null
            )
        },
        adaptation = {
            material {
                isCenterAligned = true
            }
        }
    )
}

@Composable
fun MyAdaptiveNavigationBar() {
    var selectedItem by remember { mutableStateOf(0) }

    AdaptiveNavigationBar {
        AdaptiveNavigationBarItem(
            selected = selectedItem == 0,
            onClick = { selectedItem = 0 },
            icon = {
                Icon(
                    imageVector = AdaptiveIcons.Outlined.KeyboardArrowLeft,
                    contentDescription = null
                )
            },
            enabled = true,
            label = { Text("Item 1") }
        )
        AdaptiveNavigationBarItem(
            selected = selectedItem == 1,
            onClick = { selectedItem = 1 },
            icon = {
                Icon(
                    imageVector = AdaptiveIcons.Outlined.KeyboardArrowLeft,
                    contentDescription = null
                )
            },
            enabled = true,
            label = { Text("Item 2") }
        )
    }
}