package org.umcs.mobile.composables.shared.new_uuid

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowRight

@Composable
fun CaseUUIDInput(
    modifier : Modifier = Modifier,
    text: String,
    supportingText: String,
    isFocused: Boolean,
    focusRequester: FocusRequester,
    onButtonPress : ()->Unit,
    onTextChange: (String) -> Unit,
    onFocusChange: (Boolean) -> Unit,
    onSupportingTextChange: (String) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
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
            shape = RoundedCornerShape(16.dp),
            value = text,
            onValueChange = onTextChange,
            label = { Text("Case Identifier") },
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
        FloatingActionButton(
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
                imageVector = FeatherIcons.ArrowRight,
                contentDescription = "Add Case by UUID"
            )
        }
    }
}
