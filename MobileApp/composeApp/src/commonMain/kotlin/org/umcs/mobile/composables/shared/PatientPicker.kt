package org.umcs.mobile.composables.shared

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.slapps.cupertino.CupertinoSearchTextField
import com.slapps.cupertino.adaptive.AdaptiveWidget
import org.umcs.mobile.composables.new_case_view.patientList
import org.umcs.mobile.data.Patient

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PatientPicker(
    patientList: List<Patient> = patientList(),
    pickPatient: (Patient) -> Unit,
    onDismiss: () -> Unit,
    patientPickerState: SheetState,
) {
    var searchPatient by remember { mutableStateOf("") }

    ModalBottomSheet(
        sheetState = patientPickerState,
        onDismissRequest = onDismiss
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().heightIn(min = 200.dp, max = 400.dp)
        ) {
            stickyHeader {
                AdaptiveSearchField(
                    value = searchPatient,
                    onValueChange = {
                        searchPatient = it
                    },
                    modifier = Modifier,
                    placeholder = { Text("Search...") }
                )
            }
            items(
                patientList.filter {
                    it.getFullName().contains(searchPatient, ignoreCase = true)
                }
            ) { patient ->
                Text(
                    text = patient.getFullName(),
                    modifier = Modifier.padding(12.dp).clickable {
                        pickPatient(patient)
                        onDismiss()
                    })
            }
        }
    }
}


@Composable
fun AdaptiveSearchField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: @Composable () -> Unit
) {
    AdaptiveWidget(
        material ={
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = modifier,
                placeholder = placeholder
            )
        },
        cupertino = {
            CupertinoSearchTextField(
                onValueChange = onValueChange,
                value = value,
                placeholder = placeholder,
                modifier  = modifier.fillMaxWidth(0.7f),
            )
        }
    )
}