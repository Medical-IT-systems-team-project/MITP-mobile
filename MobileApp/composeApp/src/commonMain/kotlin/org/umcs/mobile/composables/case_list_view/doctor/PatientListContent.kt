package org.umcs.mobile.composables.case_list_view.doctor

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.touchlab.kermit.Logger
import org.umcs.mobile.AdaptivePatientListItem
import org.umcs.mobile.composables.new_case_view.patientList
import org.umcs.mobile.data.Patient

@Composable
fun PatientListContent(
    onImportPatientCase: (Patient) -> Unit,
    onShareUUID: (Patient) -> Unit,
    contentPadding: PaddingValues,
    patients: List<Patient> = patientList(),
    listState: LazyListState = rememberLazyListState(),
    modifier: Modifier = Modifier,
) {
    var showDropdownForPatient by remember { mutableStateOf<Patient?>(null) }

    LazyColumn(
        contentPadding = contentPadding,
        state = listState,
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(patients) { patient ->
            AdaptivePatientListItem(
                showDropdownForPatient = showDropdownForPatient,
                patient = patient,
                onImportPatientCase = onImportPatientCase,
                onShareUUID = onShareUUID
            ) { showDropdown : Patient? ->
                showDropdownForPatient = showDropdown
            }
        }
        item { Spacer(modifier.height(5.dp)) }
    }
}


