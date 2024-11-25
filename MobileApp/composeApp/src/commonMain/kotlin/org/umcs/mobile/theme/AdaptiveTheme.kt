package org.umcs.mobile.theme

import io.github.alexzhirkevich.cupertino.adaptive.Theme

/*@OptIn(ExperimentalAdaptiveApi::class)
@Composable
internal fun AppTheme(
    systemIsDark: Boolean = isSystemInDarkTheme(),
    theme: Theme = determineTheme(),
    content: @Composable () -> Unit
) {
    Logger.i("App theme is ${theme.name}", tag= "THEME")
   AdaptiveTheme(
       target = theme,
       material = MaterialThemeSpec(
           colorScheme = if (systemIsDark) {
               darkScheme
           } else {
               lightScheme
           },
       ),
       cupertino  = CupertinoThemeSpec(
          typography = AppleTypography(),
       ),
       content = content
   )
}*/

expect fun determineTheme(): Theme
