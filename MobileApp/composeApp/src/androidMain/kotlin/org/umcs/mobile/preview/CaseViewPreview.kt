package org.umcs.mobile.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.umcs.mobile.composables.case_list_view.CaseListLayout
import org.umcs.mobile.theme.AppTheme

@Preview(showSystemUi = true)
@Composable
private fun CaseViewPreviewDark() {
    AppTheme(systemIsDark = true){
        CaseListLayout({}, {  })
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CaseViewPreviewLight() {
    AppTheme(systemIsDark = false){
        CaseListLayout({  }, {})
    }
}