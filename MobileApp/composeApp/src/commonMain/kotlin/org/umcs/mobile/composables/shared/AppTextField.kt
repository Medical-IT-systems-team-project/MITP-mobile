package org.umcs.mobile.composables.shared

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.slapps.cupertino.CupertinoBorderedTextField
import com.slapps.cupertino.adaptive.AdaptiveWidget
import com.slapps.cupertino.adaptive.Theme
import com.slapps.cupertino.theme.CupertinoTheme
import org.umcs.mobile.theme.determineTheme


@Composable
fun AdaptiveTextField(
    modifier: Modifier = Modifier,
    isSingleLine: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    title: @Composable (() -> Unit)? = null,
    text: String,
    supportingText: String,
    changeSupportingText : (String) -> Unit,
    focusRequester: FocusRequester,
    onTextChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    readOnly: Boolean = false,
    placeholder: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    AdaptiveWidget(
        cupertino = {
            CupertinoAppTextField(
                modifier = modifier,
                isSingleLine = isSingleLine,
                maxLines = maxLines,
                text = text,
                changeSupportingText = changeSupportingText,
                supportingText = supportingText,
                focusRequester = focusRequester,
                onTextChange = onTextChange,
                keyboardType = keyboardType,
                readOnly = readOnly,
                placeholder = placeholder,
                trailingIcon = trailingIcon
            )
        },
        material = {
            MaterialTextField(
                modifier = modifier,
                isSingleLine = isSingleLine,
                maxLines = maxLines,
                title = title,
                text = text,
                supportingText = supportingText,
                focusRequester = focusRequester,
                onTextChange = onTextChange,
                keyboardType = keyboardType,
                readOnly = readOnly,
                placeholder = placeholder,
                trailingIcon = trailingIcon
            )
        }
    )
}

@Composable
fun MaterialTextField(
    modifier: Modifier = Modifier,
    isSingleLine: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    title: @Composable (() -> Unit)? = null,
    text: String,
    supportingText: String,
    focusRequester: FocusRequester,
    onTextChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    readOnly: Boolean = false,
    placeholder: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    val theme = remember { determineTheme() }
    val isCupertino = when (theme) {
        Theme.Cupertino -> true
        Theme.Material3 -> false
    }
    val supportStyle =
        if (isCupertino) CupertinoTheme.typography.footnote else MaterialTheme.typography.bodySmall
    val textStyle =
        if (isCupertino) CupertinoTheme.typography.body else MaterialTheme.typography.bodyMedium
    val width = if (isCupertino) 300.dp else 270.dp

    TextField(
        textStyle = textStyle,
        maxLines = maxLines,
        trailingIcon = trailingIcon,
        placeholder = placeholder,
        readOnly = readOnly,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        supportingText = {
            Text(
                style = supportStyle,
                text = supportingText,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 16.dp)
            )
        },
        singleLine = isSingleLine,
        shape = RoundedCornerShape(16.dp),
        value = text,
        onValueChange = onTextChange,
        label = title,
        modifier = modifier
            .width(width)
            .focusRequester(focusRequester),
        colors = TextFieldDefaults.colors(
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
    )
}

@Composable
fun CupertinoAppTextField(
    modifier: Modifier = Modifier,
    isSingleLine: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    text: String,
    supportingText: String,
    focusRequester: FocusRequester,
    onTextChange: (String) -> Unit,
    changeSupportingText : (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    readOnly: Boolean = false,
    placeholder: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    val isError = supportingText.isNotBlank()
    val value = if (isError) supportingText else text
    var isFocused by remember { mutableStateOf(false) }


    CupertinoBorderedTextField(
        isError = isError,
        value = value,
        onValueChange = onTextChange,
        placeholder = placeholder,
        singleLine = isSingleLine,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        modifier = modifier.onFocusChanged { focusState ->
            isFocused = focusState.isFocused
            if (isFocused && isError) {
                changeSupportingText("")
            }
        }
            .focusRequester(focusRequester),
        readOnly = readOnly,
        trailingIcon = trailingIcon,
        maxLines = maxLines,
    )
}