package org.umcs.mobile.composables.shared

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.slapps.cupertino.adaptive.Theme
import com.slapps.cupertino.theme.CupertinoTheme
import org.umcs.mobile.theme.determineTheme


@Composable
fun ContentText(modifier: Modifier = Modifier, content: String, maxLines: Int) {
    val style = when(determineTheme()){
        Theme.Cupertino -> CupertinoTheme.typography.subhead
        Theme.Material3 -> MaterialTheme.typography.bodyMedium
    }

    Text(
        overflow = TextOverflow.Ellipsis,
        maxLines = maxLines,
        text = content,
        style = style,
    )
}