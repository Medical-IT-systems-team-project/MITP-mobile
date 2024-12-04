package org.umcs.mobile.composables.case_list_view.doctor

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun AdaptiveDropdown(
    onImportPatientCase: () -> Unit,
    onShareUUID: () -> Unit,
    onDismiss: () -> Unit,
    expanded: Boolean,
) {
    AdaptiveWidget(
        material = {
            PatientDropdownMenu(
                expanded = expanded,
                onDismiss = onDismiss,
                onImportPatientCase = onImportPatientCase,
                onShareUUID = onShareUUID,
            )
        },
        cupertino = {
            CupertinoDropdownMenu(
                containerColor = CupertinoTheme.colorScheme.secondarySystemBackground,
                expanded = expanded,
                onDismissRequest = onDismiss,
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
    )
}