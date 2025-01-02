package org.umcs.mobile.composables.case_list_view

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.slapps.cupertino.adaptive.AdaptiveNavigationBar
import com.slapps.cupertino.adaptive.AdaptiveNavigationBarItem
import com.slapps.cupertino.adaptive.icons.AdaptiveIcons
import com.slapps.cupertino.adaptive.icons.KeyboardArrowLeft
import compose.icons.FeatherIcons
import compose.icons.feathericons.File
import mobileapp.composeapp.generated.resources.Res
import mobileapp.composeapp.generated.resources.user
import org.jetbrains.compose.resources.painterResource

@Composable
fun CaseListBottomBar(
    modifier: Modifier = Modifier,
    currentTab: CaseListScreens,
    navigateToCases: () -> Unit,
    navigateToPatients: () -> Unit,
) {
    val iconSize = Modifier.size(25.dp)
    AdaptiveNavigationBar{
        AdaptiveNavigationBarItem(
            icon = {
                Icon(
                    imageVector = FeatherIcons.File,
                    contentDescription = null,
                    iconSize
                )
            },
            label = { Text("Cases") },
            selected = currentTab == CaseListScreens.CASES,
            onClick = {
                if (currentTab != CaseListScreens.CASES) {
                    navigateToCases()
                }
            }
        )
        AdaptiveNavigationBarItem(
            icon = {
                Icon(
                    painterResource(Res.drawable.user),
                    contentDescription = null,
                    iconSize
                )
            },
            label = { Text("Patients") },
            selected = currentTab == CaseListScreens.PATIENTS,
            onClick = {
                if (currentTab != CaseListScreens.PATIENTS) {
                    navigateToPatients()
                }
            }
        )
    }
}

