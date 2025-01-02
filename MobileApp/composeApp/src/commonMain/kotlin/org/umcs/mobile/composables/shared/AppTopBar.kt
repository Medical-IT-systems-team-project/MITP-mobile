@file:OptIn(ExperimentalAdaptiveApi::class)

package org.umcs.mobile.composables.shared

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import com.slapps.cupertino.adaptive.AdaptiveTopAppBar
import com.slapps.cupertino.adaptive.ExperimentalAdaptiveApi
import com.slapps.cupertino.adaptive.Theme
import com.slapps.cupertino.theme.CupertinoTheme
import org.umcs.mobile.theme.determineTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    navigateBack: (() -> Unit)? = null,
    title: String,
    actions :@Composable (RowScope.() -> Unit) = {},
    scrollBehavior: TopAppBarScrollBehavior,
) {
    val isCupertino = when (determineTheme()) {
        Theme.Cupertino -> true
        Theme.Material3 -> false
    }

    AdaptiveTopAppBar(
        actions = actions,
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        title = {
            Text(
                text = title,
                style = if (isCupertino) CupertinoTheme.typography.title2 else MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            if (navigateBack != null) {
                BackButton(navigateBack = navigateBack)
            } else {
                Unit
            }
        },
        adaptation = {
            material {
                isCenterAligned = true
            }
        }
    )
}


