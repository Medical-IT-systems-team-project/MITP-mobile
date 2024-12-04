@file:OptIn(
    ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class,
    ExperimentalMaterial3Api::class
)

package org.umcs.mobile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.slapps.cupertino.ExperimentalCupertinoApi
import com.slapps.cupertino.adaptive.AdaptiveAlertDialogNative
import com.slapps.cupertino.adaptive.AdaptiveCheckbox
import com.slapps.cupertino.adaptive.AdaptiveCircularProgressIndicator
import com.slapps.cupertino.adaptive.AdaptiveDatePicker
import com.slapps.cupertino.adaptive.AdaptiveFilledIconButton
import com.slapps.cupertino.adaptive.AdaptiveScaffold
import com.slapps.cupertino.adaptive.AdaptiveSurface
import com.slapps.cupertino.adaptive.AdaptiveTextButton
import com.slapps.cupertino.adaptive.AdaptiveWidget
import com.slapps.cupertino.adaptive.ExperimentalAdaptiveApi
import com.slapps.cupertino.adaptive.Theme
import com.slapps.cupertino.adaptive.icons.AdaptiveIcons
import com.slapps.cupertino.adaptive.icons.MailOutline
import com.slapps.cupertino.rememberCupertinoDatePickerState
import org.umcs.mobile.composables.case_list_view.doctor.PatientListItem
import org.umcs.mobile.composables.shared.AdaptiveFAB
import org.umcs.mobile.data.Patient
import org.umcs.mobile.theme.determineTheme

@Composable
fun TestAdaptive(showAlert: Boolean = false, showCalendar: Boolean = false) {
    var checkboxState by remember { mutableStateOf(false) }
    val text = when (determineTheme()) {
        Theme.Cupertino -> "Apple is here bby"
        Theme.Material3 -> "Google is here bby"
    }

    AdaptiveSurface(
        modifier = Modifier.fillMaxSize(),
    ) {
        AdaptiveScaffold(
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = { AdaptiveFAB(onClick = {}) }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.padding(it).padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState()).fillMaxSize()
            ) {
                Row {
                    Text(text)
                    AdaptiveCheckbox(
                        checked = checkboxState,
                        onCheckedChange = { checkboxState = !checkboxState }
                    )
                    AdaptiveCheckbox(
                        checked = !checkboxState,
                        onCheckedChange = { checkboxState = !checkboxState }
                    )
                }
                AdaptiveTextButton(
                    onClick = {},
                    content = { Text(text) }
                )
                if (showAlert) {
                    AdaptiveAlertDialogNative(
                        onDismissRequest = {},
                        title = "bazinga",
                        message = "bezinga",
                    ) {
                        action({}, title = "OK")
                        action({}, title = "Nie Ok")
                    }
                }
                if (showCalendar) {
                    AdaptiveDatePicker(
                        state = rememberCupertinoDatePickerState()
                    )
                }
                AdaptiveCircularProgressIndicator()

                AdaptiveFilledIconButton(
                    onClick = {},
                    content = { Icon(AdaptiveIcons.Outlined.MailOutline, null) }
                )
            }
        }
    }
}

@Composable
fun AdaptivePatientListItem(
    showDropdownForPatient: Patient?,
    patient: Patient,
    onImportPatientCase: (Patient) -> Unit,
    onShareUUID: (Patient) -> Unit,
    changeShowDropdown: (Patient?) -> Unit,
) {

    AdaptiveWidget(
        material = {
            PatientListItem(
                showDropdownForPatient = showDropdownForPatient,
                patient = patient,
                onImportPatientCase = onImportPatientCase,
                onShareUUID = onShareUUID,
                changeShowDropdown = changeShowDropdown
            )
        },
        cupertino = {
            PatientListItem(
                isCupertino = true,
                showDropdownForPatient = showDropdownForPatient,
                patient = patient,
                onImportPatientCase = onImportPatientCase,
                onShareUUID = onShareUUID,
                changeShowDropdown = changeShowDropdown
            )
        }
    )
}


