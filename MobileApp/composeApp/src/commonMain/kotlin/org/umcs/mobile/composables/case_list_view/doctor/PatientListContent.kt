package org.umcs.mobile.composables.case_list_view.doctor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.umcs.mobile.composables.new_case_view.patientList
import org.umcs.mobile.data.Patient

@Composable
fun PatientListContent(
    contentPadding: PaddingValues,
    patients: List<Patient> = patientList(),
    listState: LazyListState = rememberLazyListState(),
    modifier: Modifier = Modifier
) {
    val textColor = MaterialTheme.colorScheme.onPrimary

    LazyColumn(
        contentPadding = contentPadding,
        state = listState,
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(patients) { patient ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp)
                    .clip(shape = MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 12.dp)
                    .clickable {  }
            ) {
                Icon(Icons.Default.Face, contentDescription = "Select Patient", tint = textColor)
                Text(
                    overflow= TextOverflow.Ellipsis,
                    maxLines = 1,
                    text = patient.getFullName(),
                    fontSize = 20.sp,
                    color = textColor,
                )
            }
        }
    }
}
