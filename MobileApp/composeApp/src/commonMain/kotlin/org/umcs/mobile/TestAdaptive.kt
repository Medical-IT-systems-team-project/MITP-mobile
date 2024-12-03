@file:OptIn(ExperimentalAdaptiveApi::class)

package org.umcs.mobile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slapps.cupertino.adaptive.AdaptiveFilledIconButton
import com.slapps.cupertino.adaptive.AdaptiveScaffold
import com.slapps.cupertino.adaptive.AdaptiveTopAppBar
import com.slapps.cupertino.adaptive.ExperimentalAdaptiveApi
import com.slapps.cupertino.adaptive.icons.AdaptiveIcons

@Composable
fun TestAdaptive() {
    AdaptiveScaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {},
        bottomBar = {},
    ){
    }
}

@Composable
fun MyAdaptiveTopBar() {
    AdaptiveTopAppBar(
        title = {Text("title")},
        navigationIcon = {

        }
    )
}