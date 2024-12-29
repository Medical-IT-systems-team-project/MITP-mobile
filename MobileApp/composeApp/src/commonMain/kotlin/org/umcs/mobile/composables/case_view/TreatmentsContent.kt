@file:OptIn(ExperimentalFoundationApi::class)

package org.umcs.mobile.composables.case_view

import AppViewModel
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
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
import com.slapps.cupertino.adaptive.Theme.Cupertino
import com.slapps.cupertino.adaptive.Theme.Material3
import com.slapps.cupertino.icons.CupertinoIcons
import com.slapps.cupertino.icons.outlined.Checkmark
import com.slapps.cupertino.icons.outlined.Cross
import com.slapps.cupertino.icons.outlined.HeartTextSquare
import com.slapps.cupertino.icons.outlined.Xmark
import com.slapps.cupertino.theme.CupertinoTheme
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel
import org.umcs.mobile.composables.case_list_view.doctor.AdaptiveDropdownItem
import org.umcs.mobile.composables.case_list_view.doctor.AdaptiveDropdownMenu
import org.umcs.mobile.data.Case
import org.umcs.mobile.network.dto.case.MedicalStatus
import org.umcs.mobile.theme.determineTheme

@Composable
fun TreatmentsContent(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    case: Case,
    viewModel: AppViewModel = koinViewModel(),
) {
    val treatments = case.treatments
    val isDoctor = viewModel.isDoctor
    var showDropdownForTreatment by remember { mutableStateOf<Treatment?>(null) }

    LazyColumn(
        contentPadding = paddingValues,
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start
    ) {
        items(treatments) { treatment ->
            TreatmentItem(
                dismissDropdown = { showDropdownForTreatment = null },
                dropdownExpanded = showDropdownForTreatment == treatment,
                treatment = treatment,
                modifier = if (isDoctor) {
                    Modifier.combinedClickable(
                        onClick = {},
                        onLongClick = {
                            showDropdownForTreatment = if (treatment.status in listOf(
                                    MedicalStatus.COMPLETED,
                                    MedicalStatus.CANCELLED
                                )
                            ) null else treatment
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
fun TreatmentItem(
    modifier: Modifier = Modifier,
    treatment: Treatment,
    dropdownExpanded: Boolean,
    dismissDropdown: () -> Unit,
) {
    var showMore by remember { mutableStateOf(false) }
    val style = when (determineTheme()) {
        Cupertino -> CupertinoTheme.typography.subhead
        Material3 -> MaterialTheme.typography.bodyMedium
    }
    val dropdownItems = MedicalStatus.entries
        .filter { status -> status > treatment.status }
        .map { status ->
            AdaptiveDropdownItem(
                text = status.name,
                onClick = {
                    // TODO : change treatment status
                }
            )
        }

    Column {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionTitleText(treatment.name)
            StatusIcon(treatment.status)
            AdaptiveDropdownMenu(
                expanded = dropdownExpanded,
                onDismiss = dismissDropdown,
                adaptiveDropdownItemList = dropdownItems
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .padding(top = 4.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
                .animateContentSize(animationSpec = tween(durationMillis = 130))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { showMore = !showMore }
        ) {
            Text(text = treatment.details, style = style)
            if (showMore) {
                Text(text = "Start: ${treatment.startDate}", style = style)
                Text(text = "End: ${treatment.endDate}", style = style)
                Text(text = "Created By: ${treatment.createdBy}", style = style)
            }
        }
    }
}

@Composable
fun StatusIcon(status: MedicalStatus) {
    val statusVector = when (status) {
        MedicalStatus.PLANNED -> CupertinoIcons.Outlined.HeartTextSquare
        MedicalStatus.ONGOING -> CupertinoIcons.Outlined.Cross
        MedicalStatus.COMPLETED -> CupertinoIcons.Outlined.Checkmark
        MedicalStatus.CANCELLED -> CupertinoIcons.Outlined.Xmark
    }

    Icon(
        imageVector = statusVector,
        contentDescription = null
    )
}

@Serializable
data class Treatment(
   // val treatmentId : Int,
    val name: String,
    val startDate: String,
    val endDate: String,
    val details: String,
    val createdBy: String,
    val status: MedicalStatus,
)

fun fetchTreatment() = listOf(
    Treatment(
        name = "Antibiotic Treatment",
        startDate = "2024-01-01",
        endDate = "2024-01-15",
        details = "Prescribed amoxicillin 500mg twice daily",
        createdBy = "Dr. Smith",
        status = MedicalStatus.PLANNED,
  //      treatmentId = 1
    ),
    Treatment(
        name = "Physical Therapy",
        startDate = "2024-02-01",
        endDate = "2024-02-28",
        details = "Strengthening exercises and mobility training",
        createdBy = "Dr. Johnson",
        status = MedicalStatus.ONGOING,
    //    treatmentId = 2
    ),
    Treatment(
        name = "Blood Pressure Management",
        startDate = "2024-03-10",
        endDate = "2024-03-25",
        details = "Increased dosage of blood pressure medication",
        createdBy = "Dr. Williams",
        status = MedicalStatus.PLANNED,
   //     treatmentId = 3
    ),
    Treatment(
        name = "Therapy Sessions",
        startDate = "2024-04-05",
        endDate = "2024-04-20",
        details = "Weekly therapy sessions focusing on anxiety management",
        createdBy = "Dr. Brown",
        status = MedicalStatus.CANCELLED,
     //   treatmentId = 4
    ),
    Treatment(
        name = "Post-Surgery Care",
        startDate = "2024-05-01",
        endDate = "2024-05-15",
        details = "Daily physical exercises and wound care",
        createdBy = "Dr. Davis",
        status = MedicalStatus.COMPLETED,
      //  treatmentId = 5
    )
)