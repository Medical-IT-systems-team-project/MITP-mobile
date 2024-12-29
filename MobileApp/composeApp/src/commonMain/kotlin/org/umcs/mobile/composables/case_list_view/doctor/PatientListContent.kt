package org.umcs.mobile.composables.case_list_view.doctor

import AppViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.umcs.mobile.AdaptivePatientListItem
import org.umcs.mobile.data.Patient

@Composable
fun PatientListContent(
    onImportPatientCase: (Patient) -> Unit,
    onShareUUID: (Patient) -> Unit,
    contentPadding: PaddingValues,
    listState: LazyListState = rememberLazyListState(),
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = koinViewModel()
) {
    val patients by viewModel.patientList.collectAsStateWithLifecycle()
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


