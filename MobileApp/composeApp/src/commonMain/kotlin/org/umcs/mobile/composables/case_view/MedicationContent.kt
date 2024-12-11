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
            MedicineItem(medicine = medicine)
        }
    }
}

@Composable
fun AdaptiveMedicineDropdown() {

}

@Composable
fun MedicineItem(modifier: Modifier = Modifier, medicine: Medicine) {
    var showMore by remember { mutableStateOf(false) }
    val style = when (determineTheme()) {
        Theme.Cupertino -> CupertinoTheme.typography.subhead
        Theme.Material3 -> MaterialTheme.typography.bodyMedium
    }


    Column {
        SectionTitleText("${medicine.startDate} - ${medicine.endDate}")
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
            Text(text = "${medicine.name} - ${medicine.type}", style = style)
            if (showMore) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = medicine.dosage, style = style)
                    Text(text = medicine.frequency, style = style)
                }
                Text(text = medicine.prescribedBy, style = style)
            }
        }
    }
}

fun fetchMedicine() = listOf(
    Medicine(
        startDate = "2024-01-01",
        endDate = "2024-02-01",
        prescribedBy = "Dr. Smith",
        name = "Amoxicillin",
        dosage = "500mg",
        frequency = "3 times daily",
        type = "Antibiotic"
    ),
    Medicine(
        startDate = "2024-01-15",
        endDate = "2024-03-15",
        prescribedBy = "Dr. Johnson",
        name = "Lisinopril",
        dosage = "10mg",
        frequency = "Once daily",
        type = "Blood Pressure Medication"
    ),
    Medicine(
        startDate = "2024-02-01",
        endDate = "2024-02-14",
        prescribedBy = "Dr. Williams",
        name = "Ibuprofen",
        dosage = "400mg",
        frequency = "Every 6 hours",
        type = "Pain Reliever"
    ),
    Medicine(
        startDate = "2024-02-10",
        endDate = "2024-03-10",
        prescribedBy = "Dr. Brown",
        name = "Metformin",
        dosage = "850mg",
        frequency = "Twice daily",
        type = "Diabetes Medication"
    ),
    Medicine(
        startDate = "2024-02-15",
        endDate = "2024-04-15",
        prescribedBy = "Dr. Davis",
        name = "Atorvastatin",
        dosage = "20mg",
        frequency = "Once daily",
        type = "Cholesterol Medication"
    )
)

data class Medicine(
    val startDate: String,
    val endDate: String,
    val prescribedBy: String,
    val name: String,
    val dosage: String,
    val frequency: String,
    val type: String,
)