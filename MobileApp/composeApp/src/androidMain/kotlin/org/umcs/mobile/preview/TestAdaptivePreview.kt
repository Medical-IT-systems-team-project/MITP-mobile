package org.umcs.mobile.preview

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.slapps.cupertino.adaptive.AdaptiveTheme
import com.slapps.cupertino.adaptive.Theme
import org.umcs.mobile.TestAdaptive
import org.umcs.mobile.theme.AppTheme

@Preview
@Composable
private fun TestAdaptiveCupertino() {
    AppTheme(theme = Theme.Cupertino) {
        TestAdaptive()
    }
}

@Preview
@Composable
private fun TestAdaptiveMaterial() {
    AppTheme(theme = Theme.Material3) {
        TestAdaptive()
    }
}

@Preview
@Composable
private fun TestAdaptiveCupertinoDark() {
    AppTheme(systemIsDark = true, theme = Theme.Cupertino) {
        TestAdaptive()
    }
}

@Preview
@Composable
private fun TestAdaptiveMaterialDark() {
    AppTheme(systemIsDark = true, theme = Theme.Material3) {
        TestAdaptive()
    }
}