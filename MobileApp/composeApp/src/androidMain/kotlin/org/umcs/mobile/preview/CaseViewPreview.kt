package org.umcs.mobile.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.umcs.mobile.composables.case_list_view.loremIpsum
import org.umcs.mobile.composables.case_view.CaseLayout
import org.umcs.mobile.data.Case
import org.umcs.mobile.theme.AppTheme


val case = Case(
    caseDetails = "Dislocated shoulder",
    stringDate = "2023-04-01",
    patientName = "Bob Brownnnnnnnnnnnnnn",
    doctorName = "Dr. Brown",
    description = loremIpsum
)

val longCase = Case(
    caseDetails = "Dislocated shoulderrrrrrrrrrrrrrrrrrrrrrrrrr",
    stringDate = "2023-04-01",
    patientName = "Bob Brownnnnnnnnnnnnnn",
    doctorName = "Dr. Brown",
    description = loremIpsum
)


@Preview(showSystemUi = true)
@Composable
private fun CaseListViewPreviewDark() {
    AppTheme(systemIsDark = true) {
        CaseLayout(case, {}, true)
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CaseListViewPreviewLight() {
    AppTheme(systemIsDark = false) {
        CaseLayout(case, {}, true)
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CaseListViewPreviewDarkLong() {
    AppTheme(systemIsDark = true) {
        CaseLayout(longCase, {}, true)
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CaseListViewPreviewLightLong() {
    AppTheme(systemIsDark = false) {
        CaseLayout(longCase, {}, true)
    }
}

