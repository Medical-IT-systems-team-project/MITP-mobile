package org.umcs.mobile.composables.case_view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.umcs.mobile.composables.shared.CaseItem
import org.umcs.mobile.data.Case

@Composable
fun InfoContent(modifier: Modifier = Modifier, paddingValues : PaddingValues, case : Case) {
    Column(
        modifier = Modifier.padding(paddingValues).fillMaxSize().padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start
    ) {
        CaseItem(
            title = "Patient",
            content = case.patientName
        )
        CaseItem(
            title = "Admission date",
            content = case.stringDate
        )
        CaseItem(
            title = "Doctor",
            content = case.doctorName
        )
        CaseItem(
            title = "Diagnosis",
            content = case.description
        )
    }
}