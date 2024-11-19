package org.umcs.mobile.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.umcs.mobile.composables.new_patient_view.NewPatientLayout
import org.umcs.mobile.theme.AppTheme

@Preview(showSystemUi = true)
@Composable
fun NewPatientPreviewDark() {
    AppTheme(systemIsDark = true) {
        NewPatientLayout({})
    }
}

@Preview(showSystemUi = true)
@Composable
fun NewPatientPreviewLight() {
    AppTheme(systemIsDark = false) {
        NewPatientLayout({})
    }
}