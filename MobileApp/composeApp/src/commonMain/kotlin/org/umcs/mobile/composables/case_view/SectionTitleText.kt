package org.umcs.mobile.composables.case_view

import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slapps.cupertino.adaptive.AdaptiveWidget

@Composable
fun SectionTitleText(text : String) {
    AdaptiveWidget(
        material = {
            Text(text, style = MaterialTheme.typography.titleMedium)
        },
        cupertino = {
            Text(text, style = MaterialTheme.typography.titleSmall)
        }
    )
}