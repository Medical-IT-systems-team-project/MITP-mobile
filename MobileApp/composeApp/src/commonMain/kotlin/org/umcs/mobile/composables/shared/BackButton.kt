package org.umcs.mobile.composables.shared

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft

@Composable
fun BackButton(navigateBack: () -> Unit) {
    IconButton(
        onClick = navigateBack,
    ) {
        Icon(
            imageVector = FeatherIcons.ArrowLeft,
            contentDescription = "Back",
        )
    }
}