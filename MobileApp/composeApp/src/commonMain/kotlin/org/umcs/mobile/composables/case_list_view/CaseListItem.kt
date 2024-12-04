package org.umcs.mobile.composables.case_list_view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import com.theapache64.rebugger.Rebugger
import org.umcs.mobile.data.Case

@Composable
fun CaseListItem(
    showPatientName: Boolean,
    onCaseClicked: (Case) -> Unit,
    modifier: Modifier = Modifier,
    currentCase: Case,
) {
    val textColor = MaterialTheme.colorScheme.onPrimary

    Column(
        modifier = modifier
            .fillMaxSize()
            .clip(shape = MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.primary)
            .padding(top = 8.dp)
            .clickable {
                onCaseClicked(currentCase)
            },
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Text(
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                text = if (showPatientName) currentCase.getFullName() else currentCase.doctorName,
                fontSize = 20.sp,
                color = textColor,
                modifier = Modifier.weight(1.9f)
            )
            Text(
                maxLines = 1,
                text = currentCase.stringDate,
                fontSize = 18.sp,
                color = textColor,
                modifier = Modifier.weight(1.1f)
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

    Rebugger(
        trackMap = mapOf(
            "isDoctor" to showPatientName,
            "navigateToCase" to onCaseClicked,
            "modifier" to modifier,
            "currentCase" to currentCase,
            "textColor" to textColor,
            """modifier
                .fillMaxSize()
                .clip(shape = MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.primary)
                .padding(top = 8.dp)
                .clickable {
                    navigateToCase(currentCase)
                }"""
             to modifier
                .fillMaxSize()
                .clip(shape = MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.primary)
                .padding(top = 8.dp)
                .clickable {
                    onCaseClicked(currentCase)
                },
            "Alignment.Start" to Alignment.Start,
            "Arrangement.spacedBy(16.dp)" to Arrangement.spacedBy(16.dp),
        ),
    )
}
