@file:OptIn(ExperimentalFoundationApi::class)

package org.umcs.mobile.composables.case_view

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
import org.umcs.mobile.data.Case
import org.umcs.mobile.network.dto.case.MedicalStatus
import org.umcs.mobile.theme.determineTheme

@Composable
fun MedicationContent(paddingValues: PaddingValues, case: Case) {
    val medicineList = remember { fetchMedicine() }

    LazyColumn(
        contentPadding = paddingValues,
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start
    ) {
        items(medicineList) { medicine ->
            MedicationItem(medication = medicine)
        }
    }
}

@Composable
fun AdaptiveMedicineDropdown() {

}

@Composable
fun MedicationItem(modifier: Modifier = Modifier, medication: Medication) {
    var showMore by remember { mutableStateOf(false) }
    val style = when (determineTheme()) {
        Theme.Cupertino -> CupertinoTheme.typography.subhead
        Theme.Material3 -> MaterialTheme.typography.bodyMedium
    }

    Column {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionTitleText(medication.name)
            StatusIcon(medication.status)
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
                    onClick =  { showMore = !showMore },
                    onLongClick = {}
                )
        ) {
            Text(text = medication.dosageForm, style = style)
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

data class Medication(
    val name: String,
    val startDate: String,
    val endDate: String,
    val dosageForm : String,
    val prescribedBy: String,
    val strength : String,
    val unit : String,
    val status : MedicalStatus
)

fun fetchMedicine() = listOf(
    Medication(
        name = "Amoxicillin",
        startDate = "2024-01-01",
        endDate = "2024-02-01",
        dosageForm = "Capsule",
        strength = "500mg",
        unit = "mg",
        prescribedBy = "Dr. Smith",
        status = MedicalStatus.PLANNED
    ),
    Medication(
        name = "Lisinopril",
        startDate = "2024-01-15",
        endDate = "2024-03-15",
        dosageForm = "Tablet",
        strength = "10mg",
        unit = "mg",
        prescribedBy = "Dr. Johnson",
        status = MedicalStatus.ONGOING
    ),
    Medication(
        name = "Ibuprofen",
        startDate = "2024-02-01",
        endDate = "2024-02-14",
        dosageForm = "Tablet",
        strength = "400mg",
        unit = "mg",
        prescribedBy = "Dr. Williams",
        status = MedicalStatus.PLANNED
    ),
    Medication(
        name = "Metformin",
        startDate = "2024-02-10",
        endDate = "2024-03-10",
        dosageForm = "Tablet",
        strength = "850mg",
        unit = "mg",
        prescribedBy = "Dr. Brown",
        status = MedicalStatus.CANCELLED
    ),
    Medication(
        name = "Atorvastatin",
        startDate = "2024-02-15",
        endDate = "2024-04-15",
        dosageForm = "Tablet",
        strength = "20mg",
        unit = "mg",
        prescribedBy = "Dr. Davis",
        status = MedicalStatus.COMPLETED
    )
)
