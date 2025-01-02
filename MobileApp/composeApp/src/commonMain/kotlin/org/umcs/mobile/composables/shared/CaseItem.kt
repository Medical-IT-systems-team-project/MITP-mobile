package org.umcs.mobile.composables.shared

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.umcs.mobile.composables.case_view.SectionTitleText

@Composable
fun CaseItem(modifier: Modifier = Modifier, title: String, content: String) {
    var showMore by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
    ) {
        SectionTitleText(title)
        Column(
            modifier = Modifier
                .padding(top = 4.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
                .animateContentSize(animationSpec = tween(durationMillis = 130))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { showMore = !showMore }) {
            ContentText(
                content = content,
                maxLines = if (showMore) Int.MAX_VALUE else 3
            )
        }
    }
}