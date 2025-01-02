package org.umcs.mobile.composables.case_list_view.doctor

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slapps.cupertino.CupertinoDropdownMenu
import com.slapps.cupertino.adaptive.AdaptiveWidget
import com.slapps.cupertino.theme.CupertinoTheme

@Composable
fun PatientDropdownMenu(
    onImportPatientCase: () -> Unit,
    onShareUUID: () -> Unit,
    onDismiss: () -> Unit,
    expanded: Boolean,
) {
    Surface {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismiss
        ) {
            DropdownMenuItem(
                text = { Text("Import Patient's Case") },
                onClick = {
                    onDismiss()
                    onImportPatientCase()
                }
            )
            DropdownMenuItem(
                text = { Text("Share Patient's UUID") },
                onClick = {
                    onDismiss()
                    onShareUUID()
                }
            )
        }
    }
}

@Composable
fun AdaptivePatientDropdown(
    onImportPatientCase: () -> Unit,
    onShareUUID: () -> Unit,
    onDismiss: () -> Unit,
    expanded: Boolean,
) {
    AdaptiveDropdownMenu(
        onDismiss = onDismiss,
        expanded = expanded,
        adaptiveDropdownItemList = listOf(
            AdaptiveDropdownItem(
                text = "Import Patient's Case",
                onClick = {
                    onDismiss()
                    onImportPatientCase()
                }
            ),
            AdaptiveDropdownItem(
                text = "Share Patient's UUID",
                onClick = {
                    onDismiss()
                    onShareUUID()
                }
            )
        )
    )}

@Composable
fun AdaptiveDropdownMenu(
    onDismiss: () -> Unit,
    expanded: Boolean,
    adaptiveDropdownItemList: List<AdaptiveDropdownItem>,
) {
    AdaptiveWidget(
        material = {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = onDismiss,
            ){
                adaptiveDropdownItemList.forEach { item ->
                    DropdownMenuItem(
                        text =  { Text(item.text) },
                        onClick = item.onClick
                    )
                }
            }
        },
        cupertino = {
            CupertinoDropdownMenu(
                containerColor = CupertinoTheme.colorScheme.secondarySystemBackground,
                expanded = expanded,
                onDismissRequest = onDismiss,
            ) {
                adaptiveDropdownItemList.forEach { item ->
                    DropdownMenuItem(
                        text =  { Text(item.text) },
                        onClick = item.onClick
                    )
                }
            }
        }
    )
}

data class AdaptiveDropdownItem(
    val text: String,
    val onClick: () -> Unit,
)