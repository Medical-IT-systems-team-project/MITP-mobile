package org.umcs.mobile.composables.shared.new_uuid

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import com.slapps.cupertino.CupertinoText
import com.slapps.cupertino.CupertinoTextField
import com.slapps.cupertino.CupertinoTextFieldColors
import com.slapps.cupertino.CupertinoTextFieldDefaults
import com.slapps.cupertino.adaptive.AdaptiveFilledIconButton
import com.slapps.cupertino.adaptive.AdaptiveWidget
import com.slapps.cupertino.adaptive.Theme
import com.slapps.cupertino.adaptive.icons.AdaptiveIcons
import com.slapps.cupertino.adaptive.icons.KeyboardArrowRight
import com.slapps.cupertino.theme.CupertinoTheme
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowRight
import org.umcs.mobile.theme.determineTheme

@Composable
fun CaseUUIDInput(
    modifier: Modifier = Modifier,
    text: String,
    supportingText: String,
    isFocused: Boolean,
    focusRequester: FocusRequester,
    onButtonPress: () -> Unit,
    onTextChange: (String) -> Unit,
    onFocusChange: (Boolean) -> Unit,
    onSupportingTextChange: (String) -> Unit,
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
