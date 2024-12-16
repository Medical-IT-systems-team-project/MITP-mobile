package org.umcs.mobile.composables.case_list_view.doctor

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.umcs.mobile.composables.shared.new_uuid.NewUUIDScreen

@Composable
fun ImportNewPatient(navigateBack: () -> Unit, onSuccess: () -> Unit) {
    NewUUIDScreen(
        onSuccessButtonClick = onSuccess,
        title = "Import Patient",
        navigateBack = navigateBack,
        label = "Patient's Access ID "
    )
}