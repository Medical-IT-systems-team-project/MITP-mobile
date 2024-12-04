@file:OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)

package org.umcs.mobile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.slapps.cupertino.CupertinoSurface
import com.slapps.cupertino.ExperimentalCupertinoApi
import com.slapps.cupertino.adaptive.AdaptiveAlertDialogNative
import com.slapps.cupertino.adaptive.AdaptiveCheckbox
import com.slapps.cupertino.adaptive.AdaptiveCircularProgressIndicator
import com.slapps.cupertino.adaptive.AdaptiveDatePicker
import com.slapps.cupertino.adaptive.AdaptiveFilledIconButton
import com.slapps.cupertino.adaptive.AdaptiveNavigationBar
import com.slapps.cupertino.adaptive.AdaptiveNavigationBarItem
import com.slapps.cupertino.adaptive.AdaptiveScaffold
import com.slapps.cupertino.adaptive.AdaptiveSurface
import com.slapps.cupertino.adaptive.AdaptiveTextButton
import com.slapps.cupertino.adaptive.AdaptiveTopAppBar
import com.slapps.cupertino.adaptive.AdaptiveWidget
import com.slapps.cupertino.adaptive.ExperimentalAdaptiveApi
import com.slapps.cupertino.adaptive.Theme
import com.slapps.cupertino.adaptive.icons.AdaptiveIcons
import com.slapps.cupertino.adaptive.icons.Home
import com.slapps.cupertino.adaptive.icons.KeyboardArrowLeft
import com.slapps.cupertino.adaptive.icons.MailOutline
import com.slapps.cupertino.rememberCupertinoDatePickerState
import com.slapps.cupertino.theme.CupertinoTheme
import mobileapp.composeapp.generated.resources.Res
import mobileapp.composeapp.generated.resources.caduceus
import mobileapp.composeapp.generated.resources.caretrack
import mobileapp.composeapp.generated.resources.cross_logo
import org.jetbrains.compose.resources.painterResource
import org.umcs.mobile.composables.case_list_view.CaseListItem
import org.umcs.mobile.data.Case
import org.umcs.mobile.theme.determineTheme

@Composable
fun TestAdaptive(showAlert: Boolean = false, showCalendar: Boolean = false) {
    var checkboxState by remember { mutableStateOf(false) }
    val text = when (determineTheme()) {
        Theme.Cupertino -> "Apple is here bby"
        Theme.Material3 -> "Google is here bby"
    }

    AdaptiveSurface(
        modifier = Modifier.fillMaxSize(),
    ) {
        AdaptiveScaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { MyAdaptiveTopBar() },
            bottomBar = { MyAdaptiveNavigationBar() },
            floatingActionButton = { MyAdaptiveFAB() }
        ) {
            Column(
                modifier = Modifier.padding(it).padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState()).fillMaxSize()
            ) {
                Row {
                    Text(text)
                    AdaptiveCheckbox(
                        checked = checkboxState,
                        onCheckedChange = { checkboxState = !checkboxState }
                    )
                    AdaptiveCheckbox(
                        checked = !checkboxState,
                        onCheckedChange = { checkboxState = !checkboxState }
                    )
                }
                AdaptiveTextButton(
                    onClick = {},
                    content = { Text(text) }
                )
                if (showAlert) {
                    AdaptiveAlertDialogNative(
                        onDismissRequest = {},
                        title = "bazinga",
                        message = "bezinga",
                    ) {
                        action({}, title = "OK")
                        action({}, title = "Nie Ok")
                    }
                }
                if (showCalendar) {
                    AdaptiveDatePicker(
                        state = rememberCupertinoDatePickerState()
                    )
                }
                AdaptiveCircularProgressIndicator()

                AdaptiveFilledIconButton(
                    onClick = {},
                    content = { Icon(AdaptiveIcons.Outlined.MailOutline, null) }
                )

                AdaptiveCase()
            }
        }
    }
}

@Composable
fun MyAdaptiveFAB(icon: ImageVector = AdaptiveIcons.Outlined.Home) {
    val cupertinoBackground = CupertinoTheme.colorScheme.systemGroupedBackground
    val cupertinoContainer = CupertinoTheme.colorScheme.accent
    val cupertinoContent = CupertinoTheme.colorScheme.label
    val customElevation = FloatingActionButtonDefaults.elevation(
        defaultElevation = 12.dp,
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
                        colors = listOf(cupertinoContainer, cupertinoContainer.copy(alpha = 0.6f))
                    ),
                    CircleShape
                ),
                containerColor = Color.Transparent,
                contentColor = cupertinoContent.copy(alpha = 0.7f),
                onClick = {},
                content = { Icon(icon, null) }
            )
        }
    )
}

@Composable
fun MyAdaptiveTopBar() {
    AdaptiveTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy((-30).dp),
                modifier = Modifier.height(50.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.cross_logo),
                    contentDescription = null,
                )
                Icon(
                    painter = painterResource(Res.drawable.caretrack),
                    contentDescription = null
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = AdaptiveIcons.Outlined.KeyboardArrowLeft,
                    contentDescription = null
                )
            }
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

@Composable
fun AdaptiveCase(modifier: Modifier = Modifier) {
    val case = Case(
        stringDate = "22-2-2000",
        patientName = "Jan",
        doctorName = "Kowalski",
        caseDetails = "Sprained Ankle",
        description = ""
    )

    AdaptiveWidget(
        material = {
            CaseListItem(
                showPatientName = true,
                onCaseClicked = {},
                currentCase = case
            )
        },
        cupertino = {
            Box(
                modifier = Modifier.fillMaxWidth().height(40.dp).clip(CupertinoTheme.shapes.large)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(CupertinoTheme.colorScheme.systemGroupedBackground, CupertinoTheme.colorScheme.systemGroupedBackground.copy(alpha= 0.66f))
                        ),
                        CupertinoTheme.shapes.large
                    ),
            ) {
                Text("CUPERTINO", color = CupertinoTheme.colorScheme.systemFill)
            }
        }
    )
}

