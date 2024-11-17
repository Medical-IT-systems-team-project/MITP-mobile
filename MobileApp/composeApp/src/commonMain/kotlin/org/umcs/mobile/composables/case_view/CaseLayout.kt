package org.umcs.mobile.composables.case_view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.umcs.mobile.data.Case

@Composable
fun CaseLayout(case: Case) {
    Column(
        modifier = Modifier.fillMaxSize()
    ){
        Text(text = "UUID: ${case.caseDetails}")
        Text(text = "Date: ${case.stringDate}")
    }
}