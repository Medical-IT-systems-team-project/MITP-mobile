package org.umcs.mobile.preview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.umcs.mobile.composables.case_list_view.doctor.PatientListContent
import org.umcs.mobile.data.Patient
import org.umcs.mobile.theme.AppTheme

@Preview(showSystemUi = true)
@Composable
fun PatientViewContentDarkPreview() {
    AppTheme(systemIsDark = true) {
        val contentPadding = PaddingValues()
        val patient  = Patient("John Doe", "1234567890")
        PatientListContent(
            onImportPatientCase = { TODO() },
            onShareUUID = { TODO() },
            contentPadding = contentPadding,
            patients = listOf(patient),
            listState = rememberLazyListState(),
        )
    }
}@Preview(showSystemUi = true)
@Composable
fun PatientViewContentLightPreview() {
    AppTheme(systemIsDark = false) {
        val contentPadding = PaddingValues()
        val patient  = Patient("John Doe", "1234567890")
        PatientListContent(
            onImportPatientCase = { TODO() },
            onShareUUID = { TODO() },
            contentPadding = contentPadding,
            patients = listOf(patient),
            listState = rememberLazyListState(),
        )
    }
}