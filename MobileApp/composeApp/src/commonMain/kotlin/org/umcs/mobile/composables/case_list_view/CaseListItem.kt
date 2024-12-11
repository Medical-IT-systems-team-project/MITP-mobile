package org.umcs.mobile.composables.case_list_view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slapps.cupertino.adaptive.AdaptiveWidget
import com.slapps.cupertino.theme.CupertinoTheme
import com.theapache64.rebugger.Rebugger
import org.umcs.mobile.data.Case

@Composable
fun CaseListItem(
    showPatientName: Boolean,
    onCaseClicked: (Case) -> Unit,
    modifier: Modifier = Modifier,
    currentCase: Case,
    isCupertino: Boolean = false,
) {
    val textColor = MaterialTheme.colorScheme.onPrimary
    val shape = if (isCupertino) CupertinoTheme.shapes.extraLarge else MaterialTheme.shapes.medium
    val verticalSpacing = if (isCupertino) 12.dp else 16.dp
    val nameAndDateStyle = if (isCupertino) CupertinoTheme.typography.title3 else MaterialTheme.typography.titleMedium
    val detailsStyle = if (isCupertino) CupertinoTheme.typography.callout else MaterialTheme.typography.labelLarge
    val itemModifier = modifier.then(
        if(isCupertino) Modifier.fillMaxWidth(0.95f).height(70.dp) else Modifier.fillMaxWidth(0.8f).height(80.dp)
    )

    Column(
        modifier = itemModifier
            .fillMaxSize()
            .clip(shape)
            .background(MaterialTheme.colorScheme.primary)
            .padding(top = 8.dp, bottom = 4.dp)
            .clickable {
                onCaseClicked(currentCase)
            },
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(verticalSpacing)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Text(
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                text = if (showPatientName) currentCase.patientName else currentCase.doctorName,
                style = nameAndDateStyle,
                color = textColor,
                modifier = Modifier.weight(1.9f)
            )
            Text(
                maxLines = 1,
                textAlign = TextAlign.End,
                text = currentCase.stringDate,
                style = nameAndDateStyle,
                color = textColor,
                modifier = Modifier.weight(1.1f)
            )
        }
        Text(
            style = detailsStyle,
            maxLines = 1,
            text = currentCase.caseDetails,
            color = textColor,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

@Composable
fun AdaptiveCase(
    showPatientName: Boolean,
    onCaseClicked: (Case) -> Unit,
    modifier: Modifier = Modifier,
    currentCase: Case,
) {
    AdaptiveWidget(
        material = {
            CaseListItem(
                modifier = modifier,
                showPatientName = showPatientName,
                onCaseClicked = onCaseClicked,
                currentCase = currentCase
            )
        },
        cupertino = {
            CaseListItem(
                modifier = modifier,
                showPatientName = showPatientName,
                onCaseClicked = onCaseClicked,
                currentCase = currentCase,
                isCupertino = true
            )
        }
    )
}
