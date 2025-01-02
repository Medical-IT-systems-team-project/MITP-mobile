package org.umcs.mobile.composables.case_list_view.doctor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.slapps.cupertino.adaptive.icons.AdaptiveIcons
import com.slapps.cupertino.adaptive.icons.Face
import com.slapps.cupertino.theme.CupertinoTheme
import org.umcs.mobile.data.Patient

@Composable
fun PatientListItem(
    isCupertino: Boolean = false,
    patient: Patient,
    onShareUUID: (Patient) -> Unit,
) {
    val textColor = MaterialTheme.colorScheme.onPrimary
    val shape = if (isCupertino) CupertinoTheme.shapes.extraLarge else MaterialTheme.shapes.medium
    val nameStyle =
        if (isCupertino) CupertinoTheme.typography.title3 else MaterialTheme.typography.titleMedium

    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .then(
                if(isCupertino) Modifier.fillMaxWidth(0.95f).height(45.dp) else  Modifier.fillMaxWidth(0.8f).height(50.dp)
            )
            .clip(shape)
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 12.dp)
            .clickable(onClick = { onShareUUID(patient) })
    ) {
        Icon(AdaptiveIcons.Outlined.Face, contentDescription = "Select Patient", tint = textColor)
        Text(
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            text = patient.getFullName(),
            style = nameStyle,
            color = textColor,
        )
    }
}