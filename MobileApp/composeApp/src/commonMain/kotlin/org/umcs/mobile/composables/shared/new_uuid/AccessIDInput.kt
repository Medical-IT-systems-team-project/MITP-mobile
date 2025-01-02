package org.umcs.mobile.composables.shared.new_uuid

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import com.slapps.cupertino.CupertinoIconButton
import com.slapps.cupertino.adaptive.AdaptiveFilledIconButton
import com.slapps.cupertino.adaptive.AdaptiveWidget
import com.slapps.cupertino.adaptive.icons.AdaptiveIcons
import com.slapps.cupertino.adaptive.icons.KeyboardArrowRight
import com.slapps.cupertino.icons.CupertinoIcons
import com.slapps.cupertino.icons.outlined.Key
import org.umcs.mobile.composables.shared.AdaptiveTextField

@Composable
fun AdaptiveAccessIdInput(
    modifier: Modifier = Modifier,
    text: String,
    supportingText: String,
    isFocused: Boolean,
    focusRequester: FocusRequester,
    onButtonPress: () -> Unit,
    onTextChange: (String) -> Unit,
    onFocusChange: (Boolean) -> Unit,
    onSupportingTextChange: (String) -> Unit,
    label: String,
) {
    AdaptiveWidget(
        material = {
            MaterialAccessIdInput(
                modifier = modifier,
                text = text,
                supportingText = supportingText,
                isFocused = isFocused,
                focusRequester = focusRequester,
                onButtonPress = onButtonPress,
                onTextChange = onTextChange,
                onFocusChange = onFocusChange,
                onSupportingTextChange = onSupportingTextChange,
                label = label
            )
        },
        cupertino = {
            CupertinoAccessIDInput(
                modifier = modifier,
                text = text,
                supportingText = supportingText,
                isFocused = isFocused,
                focusRequester = focusRequester,
                onButtonPress = onButtonPress,
                onTextChange = onTextChange,
                onFocusChange = onFocusChange,
                onSupportingTextChange = onSupportingTextChange,
                label = label
            )
        }
    )
}

@Composable
fun MaterialAccessIdInput(
    modifier: Modifier = Modifier,
    text: String,
    supportingText: String,
    isFocused: Boolean,
    focusRequester: FocusRequester,
    onButtonPress: () -> Unit,
    onTextChange: (String) -> Unit,
    onFocusChange: (Boolean) -> Unit,
    onSupportingTextChange: (String) -> Unit,
    label: String,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        TextField(
            supportingText = {
                Text(
                    supportingText,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(start = 16.dp)
                )
            },
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            value = text,
            onValueChange = onTextChange,
            label = { Text(label) },
            enabled = !matchAccessID(text),
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester)
                .onFocusChanged {
                    Logger.i(it.isFocused.toString(), tag = "onFocusChanged")
                    onFocusChange(it.isFocused)

                    if (it.isFocused) {
                        onSupportingTextChange("")
                    }
                },
            colors = TextFieldDefaults.colors(
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
        )
        AdaptiveFilledIconButton(
            modifier = Modifier.size(56.dp),
            onClick = {
                onButtonPress()
            },
        ) {
            Icon(
                imageVector = AdaptiveIcons.Outlined.KeyboardArrowRight,
                contentDescription = "Add Case by UUID"
            )
        }
    }
}

@Composable
fun CupertinoAccessIDInput(
    modifier: Modifier = Modifier,
    text: String,
    supportingText: String,
    isFocused: Boolean,
    focusRequester: FocusRequester,
    onButtonPress: () -> Unit,
    onTextChange: (String) -> Unit,
    onFocusChange: (Boolean) -> Unit,
    onSupportingTextChange: (String) -> Unit,
    label: String,
) {
    val isError = supportingText.isNotBlank()
    val value = if (isError) supportingText else text

    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AdaptiveTextField(
            text= text,
            onTextChange = onTextChange,
            placeholder = {Text(label)},
            modifier = modifier
                .weight(1f)
                .onFocusChanged {
                    Logger.i(it.isFocused.toString(), tag = "onFocusChanged")
                    onFocusChange(it.isFocused)

                    if (isFocused && isError) {
                        onSupportingTextChange("")
                    }
                }
                .focusRequester(focusRequester),
            leadingIcon = {Icon(CupertinoIcons.Outlined.Key,null)},
            isSingleLine = true,
            supportingText = supportingText,
            changeSupportingText = onSupportingTextChange,
            focusRequester =  focusRequester
        )
        CupertinoIconButton(
            modifier = Modifier.size(56.dp),
            onClick = {
                onButtonPress()
            },
        ) {
            Icon(
                imageVector = AdaptiveIcons.Outlined.KeyboardArrowRight,
                contentDescription = "Add Case by UUID"
            )
        }
    }
}