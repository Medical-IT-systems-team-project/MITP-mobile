package org.umcs.mobile.preview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.umcs.mobile.composables.share_uuid_view.ShareUUIDLayout
import org.umcs.mobile.theme.AppTheme

@Preview(showSystemUi = true)
@Composable
fun ShareUUIDDarkPreview() {
    AppTheme(systemIsDark = true) {
        ShareUUIDLayout({}, modifier = Modifier.fillMaxSize() )
    }
}

@Preview(showSystemUi = true)
@Composable
fun ShareUUIDLightPreview() {
    AppTheme(systemIsDark = false) {
        ShareUUIDLayout({}, modifier = Modifier.fillMaxSize() )
    }
}