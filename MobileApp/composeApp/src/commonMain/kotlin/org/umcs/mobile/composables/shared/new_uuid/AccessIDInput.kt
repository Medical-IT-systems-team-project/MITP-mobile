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
import com.slapps.cupertino.adaptive.AdaptiveFilledIconButton
import com.slapps.cupertino.adaptive.icons.AdaptiveIcons
import com.slapps.cupertino.adaptive.icons.KeyboardArrowRight

@Composable
fun AccessIDInput(
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
            enabled = !matchUUID(text),
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
            trailingIcon = {
                if (!matchUUID(text) && text.isNotEmpty() && !isFocused) {
                    ErrorIcon()
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
                when {
                    text.isEmpty() -> onSupportingTextChange("Please enter the identification")
                    !matchUUID(text) -> onSupportingTextChange("Please enter a valid UUID")
                    matchUUID(text) -> {
                        onSupportingTextChange("CORRECT")
                        onButtonPress()
                    }
                }
            },
        ) {
            Icon(
                imageVector = AdaptiveIcons.Outlined.KeyboardArrowRight,
                contentDescription = "Add Case by UUID"
            )
        }
    }
}
