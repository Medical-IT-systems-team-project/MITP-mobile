package org.umcs.mobile.composables.case_list_view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.slapps.cupertino.adaptive.AdaptiveTopAppBar
import mobileapp.composeapp.generated.resources.Res
import mobileapp.composeapp.generated.resources.caduceus
import mobileapp.composeapp.generated.resources.caretrack
import mobileapp.composeapp.generated.resources.cross_logo
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAdaptiveTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    isDoctor: Boolean,
    navigationIcon: @Composable () -> Unit,
) {
    AdaptiveTopAppBar(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy((-30).dp),
                modifier = Modifier.height(50.dp)
            ) {
                if (isDoctor) {
                    Icon(
                        painter = painterResource(Res.drawable.caduceus),
                        contentDescription = null,
                    )
                } else {
                    Icon(
                        painter = painterResource(Res.drawable.cross_logo),
                        contentDescription = null,
                    )
                }
                Icon(
                    painter = painterResource(Res.drawable.caretrack),
                    contentDescription = null
                )
            }
        },
        navigationIcon = navigationIcon,
        adaptation = {
            material {
                isCenterAligned = true
            }
        }
    )
}