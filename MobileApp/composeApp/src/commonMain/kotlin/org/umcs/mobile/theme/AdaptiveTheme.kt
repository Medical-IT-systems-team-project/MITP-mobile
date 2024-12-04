package org.umcs.mobile.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import co.touchlab.kermit.Logger
import com.slapps.cupertino.adaptive.AdaptiveTheme
import com.slapps.cupertino.adaptive.CupertinoThemeSpec
import com.slapps.cupertino.adaptive.ExperimentalAdaptiveApi
import com.slapps.cupertino.adaptive.MaterialThemeSpec
import com.slapps.cupertino.adaptive.Theme
import com.slapps.cupertino.theme.CupertinoTheme
import com.slapps.cupertino.theme.darkColorScheme
import com.slapps.cupertino.theme.lightColorScheme


@OptIn(ExperimentalAdaptiveApi::class)
@Composable
internal fun AppTheme(
    systemIsDark: Boolean = isSystemInDarkTheme(),
    theme: Theme = determineTheme(),
    content: @Composable () -> Unit
) {
    Logger.i("App theme is ${theme.name}", tag = "THEME")
    AdaptiveTheme(
        target = theme,
        material = MaterialThemeSpec(
            colorScheme = if (systemIsDark) {
                darkScheme
            } else {
                lightScheme
            },
        ),
        cupertino = CupertinoThemeSpec(
            colorScheme = if (systemIsDark) {
                darkColorScheme(
                    accent = primaryLight,
                    systemBackground = backgroundDark ,//background,
                    systemFill = onPrimaryDark,//to samo co tekst
                    label = onSurfaceDark ,//zwykly text i ikonki
                    systemGroupedBackground = primaryDark,
                    opaqueSeparator = onSecondaryContainerDark.copy(alpha= 0.5f), //Separator pod topbarem i nad navigation barem
                    tertiarySystemBackground = backgroundDark,  //topbar i navbar
                )
            } else {
                lightColorScheme(
                    accent = primaryLight,
                    tertiarySystemBackground = backgroundLight,
                    systemFill = onPrimaryLight,//to samo co tekst
                    opaqueSeparator = onSecondaryContainerLight.copy(alpha= 0.5f), //Separator pod topbarem i nad navigation barem
                    label = onSurfaceLight ,//zwykly text i ikonki
                    systemGroupedBackground = primaryLight,
                    systemBackground = backgroundLight ,//background,
                )
            },
            typography = AppleTypography(),
        ),
        content = content
    )
}

expect fun determineTheme(): Theme
