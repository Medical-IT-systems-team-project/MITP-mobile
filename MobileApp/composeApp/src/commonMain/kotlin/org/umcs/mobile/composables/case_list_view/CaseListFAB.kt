package org.umcs.mobile.composables.case_list_view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mobileapp.composeapp.generated.resources.Res
import mobileapp.composeapp.generated.resources.ic_cyclone
import mobileapp.composeapp.generated.resources.ic_rotate_right
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun CaseListFAB(
    isDoctor: Boolean = false
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        if (isDoctor){
            ActionButton(iconResource = Res.drawable.ic_cyclone, onClick = {})
        }

        ActionButton(iconResource = Res.drawable.ic_rotate_right, onClick = {})
    }
}

@Composable
fun ActionButton(iconResource: DrawableResource, onClick: () -> Unit) {

    FloatingActionButton(
        modifier = Modifier.size(70.dp),
        onClick = onClick,
    ) {
        Icon(
            painter = painterResource(iconResource),
            contentDescription = null
        )
    }
}

