package org.umcs.mobile.composables.case_list_view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.umcs.mobile.Data.Case

@Composable
fun CaseListItem(
    modifier: Modifier = Modifier,
    case: Case
) {
    val leftPaddedText = Modifier.padding(start = 12.dp)
    val textColor = MaterialTheme.colorScheme.onPrimary

    Box(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(top = 8.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = case.stringDate, fontSize = 20.sp, modifier = leftPaddedText, color = textColor)
            Text(text = case.uuid, fontSize = 18.sp, modifier = leftPaddedText, color = textColor)
        }
    }
}