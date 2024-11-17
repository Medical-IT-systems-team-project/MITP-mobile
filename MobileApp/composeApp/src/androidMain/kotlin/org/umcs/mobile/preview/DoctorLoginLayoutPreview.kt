package org.umcs.mobile.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.umcs.mobile.composables.login.DoctorLoginLayout
import org.umcs.mobile.theme.AppTheme

@Preview(showSystemUi = true)
@Composable
fun DoctorLoginDarkPreview() {
    AppTheme(systemIsDark = true) {
        DoctorLoginLayout({},  )
    }
}

@Preview(showSystemUi = true)
@Composable
fun DoctorLoginLightPreview() {
    AppTheme(systemIsDark = false) {
        DoctorLoginLayout({},  )
    }
}