package org.umcs.mobile.composables.case_view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.umcs.mobile.data.Case

@Composable
fun MedicationContent(paddingValues: PaddingValues, case: Case) {
    Text("Medication", modifier = Modifier.padding(paddingValues))
}