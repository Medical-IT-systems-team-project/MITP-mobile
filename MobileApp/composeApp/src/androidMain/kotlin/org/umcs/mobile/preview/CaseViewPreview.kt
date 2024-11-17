package org.umcs.mobile.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.umcs.mobile.composables.case_list_view.CaseListLayout
import org.umcs.mobile.theme.AppTheme

@Preview(showSystemUi = true)
@Composable
private fun CaseViewPreviewDarkDoctor() {
    AppTheme(systemIsDark = true) {
        CaseListLayout({}, { })
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CaseViewPreviewLightDoctor() {
    AppTheme(systemIsDark = false) {
        CaseListLayout({ }, {})
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CaseViewPreviewDarkPatient() {
    AppTheme(systemIsDark = true) {
        CaseListLayout({}, { }, isDoctor = false)
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CaseViewPreviewLightPatient() {
    AppTheme(systemIsDark = false) {
        CaseListLayout({ }, {}, isDoctor = false)
    }
}