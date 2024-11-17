package org.umcs.mobile.composables.case_list_view.patient

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.umcs.mobile.data.Case

@Composable
fun CaseListItemForPatient(
    navigateToCase: (Case) -> Unit,
    modifier: Modifier = Modifier,
    currentCase: Case,
) {
    val textColor = MaterialTheme.colorScheme.onPrimary

    Box(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.primary)
            .clickable {
                navigateToCase(currentCase)
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(top = 8.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Text(
                    overflow= TextOverflow.Ellipsis,
                    maxLines = 1,
                    text = currentCase.doctorName,
                    fontSize = 20.sp,
                    color = textColor,
                    modifier = Modifier.weight(2f)
                )
                Text(
                    maxLines = 1,
                    text = currentCase.stringDate,
                    fontSize = 20.sp,
                    color = textColor,
                    modifier = Modifier.weight(1f)
                )
            }
            Text(
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                text = currentCase.caseDetails,
                fontSize = 16.sp,
                color = textColor,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}