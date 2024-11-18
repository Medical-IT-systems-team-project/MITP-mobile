package org.umcs.mobile.composables.case_view

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow


@Composable
fun ContentText(modifier: Modifier = Modifier, content: String, maxLines: Int) {
    Text(
        overflow = TextOverflow.Ellipsis,
        maxLines = maxLines,
        text = content,
        style = MaterialTheme.typography.bodyMedium,
    )
}