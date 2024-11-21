package org.umcs.mobile.preview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.umcs.mobile.composables.new_case_view.NewCaseLayout
import org.umcs.mobile.theme.AppTheme


@Preview(showSystemUi = true)
@Composable
fun NewCaseViewDarkPreview() {
    AppTheme(systemIsDark = true) {
        NewCaseLayout({}, modifier = Modifier.fillMaxSize() )
    }
}

@Preview(showSystemUi = true)
@Composable
fun NewCaseViewLightPreview() {
    AppTheme(systemIsDark = false) {
        NewCaseLayout({}, modifier = Modifier.fillMaxSize() )
    }
}