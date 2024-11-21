package org.umcs.mobile.preview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.umcs.mobile.composables.login.ChooseProfileLayout
import org.umcs.mobile.theme.AppTheme

@Preview(showSystemUi = true)
@Composable
private fun ChooseProfileLayoutDarkPreview() {
    AppTheme(systemIsDark = true) {
        ChooseProfileLayout(
            Modifier.fillMaxSize(),
            navigateToPatientLogin = {},
            navigateToDoctorLogin = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ChooseProfileLayoutLightPreview() {
    AppTheme(systemIsDark = false) {
        ChooseProfileLayout(
            Modifier.fillMaxSize(),
            navigateToPatientLogin = {},
            navigateToDoctorLogin = {}
        )
    }
}