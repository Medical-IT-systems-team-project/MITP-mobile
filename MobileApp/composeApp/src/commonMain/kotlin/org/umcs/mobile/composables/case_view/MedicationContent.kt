@file:OptIn(ExperimentalFoundationApi::class)

package org.umcs.mobile.composables.case_view

import AppViewModel
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.slapps.cupertino.adaptive.Theme
import com.slapps.cupertino.theme.CupertinoTheme
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel
import org.umcs.mobile.composables.case_list_view.doctor.AdaptiveDropdownItem
import org.umcs.mobile.composables.case_list_view.doctor.AdaptiveDropdownMenu
import org.umcs.mobile.data.Case
import org.umcs.mobile.network.dto.case.MedicalStatus
import org.umcs.mobile.theme.determineTheme

@Composable
fun MedicationContent(
    paddingValues: PaddingValues,
    case: Case,
    viewModel: AppViewModel = koinViewModel()
) {
    val medicineList = case.medications
    val isDoctor = viewModel.isDoctor
    var showDropdownForMedication by remember { mutableStateOf<Medication?>(null) }

    LazyColumn(
        contentPadding = paddingValues,
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start
    ) {
        items(medicineList) { medication ->
            MedicationItem(
                dismissDropdown = { showDropdownForMedication = null },
                dropdownExpanded = showDropdownForMedication == medication,
                medication = medication,
                modifier = if (isDoctor) {
                    Modifier.combinedClickable(
                        onClick = {},
                        onLongClick = {
                            showDropdownForMedication = if (medication.status in listOf(
                                    MedicalStatus.COMPLETED,
                                    MedicalStatus.CANCELLED
                                )
                            ) null else medication
                        }
                    )
                } else {
                    Modifier
                }
            )
        }
    }
}

@Composable
fun MedicationItem(
    modifier: Modifier = Modifier,
    medication: Medication,
    dismissDropdown: () -> Unit,
    dropdownExpanded: Boolean
) {
    var showMore by remember { mutableStateOf(false) }
    val style = when (determineTheme()) {
        Theme.Cupertino -> CupertinoTheme.typography.subhead
        Theme.Material3 -> MaterialTheme.typography.bodyMedium
    }
    val dropdownItems = MedicalStatus.entries
        .filter { status -> status > medication.status }
        .map { status ->
            AdaptiveDropdownItem(
                text = status.name,
                onClick = {
                    // TODO : change medication status
                }
            )
        }

    Column {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionTitleText(medication.name)
            StatusIcon(medication.status)
            AdaptiveDropdownMenu(
                expanded = dropdownExpanded,
                onDismiss = dismissDropdown,
                adaptiveDropdownItemList = dropdownItems
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = modifier
                .padding(top = 4.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
                .animateContentSize(animationSpec = tween(durationMillis = 130))
                .combinedClickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { showMore = !showMore },
                    onLongClick = {}
                )
        ) {
            Text(text = "${medication.dosage} ${medication.frequency}", style = style)
            if (showMore) {
                Text(text = "Start: ${medication.startDate}", style = style)
                Text(text = "End: ${medication.endDate}", style = style)
                Text(text = "Prescribed By: ${medication.prescribedBy}", style = style)
                Text(text = "Strength: ${medication.strength}", style = style)
                Text(text = "Unit: ${medication.unit}", style = style)
            }
        }
    }
}

@Serializable
data class Medication(
    val name: String,
    val startDate: String,
    val endDate: String,
    val dosage: String,
    val frequency : String,
    val prescribedBy: String,
    val strength: String,
    val unit: String,
    val status: MedicalStatus,
)

fun fetchMedicine() = listOf(
    Medication(
        name = "Amoxicillin",
        startDate = "2024-01-01",
        endDate = "2024-02-01",
        dosage = "Capsule",
        strength = "500mg",
        unit = "mg",
        prescribedBy = "Dr. Smith",
        status = MedicalStatus.PLANNED,
        frequency = "once a day "
    ),
    Medication(
        name = "Lisinopril",
        startDate = "2024-01-15",
        endDate = "2024-03-15",
        dosage = "Tablet",
        strength = "10mg",
        unit = "mg",
        prescribedBy = "Dr. Johnson",
        status = MedicalStatus.ONGOING,
        frequency = "once a day "
    ),
    Medication(
        name = "Ibuprofen",
        startDate = "2024-02-01",
        endDate = "2024-02-14",
        dosage = "Tablet",
        strength = "400mg",
        unit = "mg",
        prescribedBy = "Dr. Williams",
        status = MedicalStatus.PLANNED,
        frequency = "once a day "
    ),
    Medication(
        name = "Metformin",
        startDate = "2024-02-10",
        endDate = "2024-03-10",
        dosage = "Tablet",
        strength = "850mg",
        unit = "mg",
        prescribedBy = "Dr. Brown",
        status = MedicalStatus.CANCELLED,
        frequency = "once a day "
    ),
    Medication(
        name = "Atorvastatin",
        startDate = "2024-02-15",
        endDate = "2024-04-15",
        dosage = "Tablet",
        strength = "20mg",
        unit = "mg",
        prescribedBy = "Dr. Davis",
        status = MedicalStatus.COMPLETED,
        frequency = "once a day "
    )
)
