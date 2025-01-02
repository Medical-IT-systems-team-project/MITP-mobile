package org.umcs.mobile.composables.case_view

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.slapps.cupertino.adaptive.AdaptiveNavigationBar
import com.slapps.cupertino.adaptive.AdaptiveNavigationBarItem
import mobileapp.composeapp.generated.resources.Res
import mobileapp.composeapp.generated.resources.info
import mobileapp.composeapp.generated.resources.pill
import mobileapp.composeapp.generated.resources.stethoscope
import org.jetbrains.compose.resources.painterResource

@Composable
fun CaseViewBottomBar(
    modifier: Modifier = Modifier,
    currentTab: CaseScreens,
    navigateToInfo: () -> Unit,
    navigateToTreatments: () -> Unit,
    navigateToMedication: () -> Unit
) {
    val iconSize = Modifier.size(25.dp)
    AdaptiveNavigationBar {
        AdaptiveNavigationBarItem(
            icon = { Icon(painterResource(Res.drawable.info), contentDescription = null,iconSize) },
            label = { Text("Info") },
            selected = currentTab == CaseScreens.INFO,
            onClick = {
                if(currentTab != CaseScreens.INFO) {
                    navigateToInfo()
                }
            }
        )
        AdaptiveNavigationBarItem(
            icon = { Icon(painterResource(Res.drawable.stethoscope), contentDescription = null, iconSize) },
            label = { Text("Treatments") },
            selected = currentTab == CaseScreens.TREATMENTS,
            onClick = {
                if(currentTab != CaseScreens.TREATMENTS) {
                    navigateToTreatments()
                }
            }
        )
        AdaptiveNavigationBarItem(
            icon = { Icon(painterResource(Res.drawable.pill), contentDescription = null,iconSize) },
            label = { Text("Medication") },
            selected = currentTab == CaseScreens.MEDICATIONS,
            onClick = {
                if(currentTab != CaseScreens.MEDICATIONS) {
                    navigateToMedication()
                }
            }
        )
    }
}