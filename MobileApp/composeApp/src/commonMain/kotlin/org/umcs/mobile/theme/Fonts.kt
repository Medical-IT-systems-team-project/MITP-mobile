package org.umcs.mobile.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import io.github.alexzhirkevich.cupertino.theme.Typography
import mobileapp.composeapp.generated.resources.Res
import mobileapp.composeapp.generated.resources.SF_Pro
import org.jetbrains.compose.resources.Font

@Composable
fun AppleFont() = FontFamily(
    Font(Res.font.SF_Pro)
)

@Composable
fun AppleTypography() = Typography(
    largeTitle = TextStyle(
        fontFamily = AppleFont(),
        fontSize = 34.sp,
        lineHeight = 41.sp
    ),
    title1 = TextStyle(
        fontFamily = AppleFont(),
        fontSize = 28.sp,
        lineHeight = 34.sp
    ),
    title2 = TextStyle(
        fontFamily = AppleFont(),
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    title3 = TextStyle(
        fontFamily = AppleFont(),
        fontSize = 20.sp,
        lineHeight = 25.sp
    ),
    headline = TextStyle(
        fontFamily = AppleFont(),
        fontSize = 17.sp,
        lineHeight = 22.sp,
        fontWeight = FontWeight.SemiBold
    ),
    body = TextStyle(
        fontFamily = AppleFont(),
        fontSize = 17.sp,
        lineHeight = 22.sp
    ),
    callout = TextStyle(
        fontFamily = AppleFont(),
        fontSize = 16.sp,
        lineHeight = 21.sp
    ),
    subhead = TextStyle(
        fontFamily = AppleFont(),
        fontSize = 15.sp,
        lineHeight = 20.sp
    ),
    footnote = TextStyle(
        fontFamily = AppleFont(),
        fontSize = 13.sp,
        lineHeight = 18.sp
    ),
    caption1 = TextStyle(
        fontFamily = AppleFont(),
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    caption2 = TextStyle(
        fontFamily = AppleFont(),
        fontSize = 11.sp,
        lineHeight = 13.sp
    )
)