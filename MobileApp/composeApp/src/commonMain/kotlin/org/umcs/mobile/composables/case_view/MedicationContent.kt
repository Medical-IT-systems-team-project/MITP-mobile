package org.umcs.mobile.composables.case_view

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import org.umcs.mobile.data.Case

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
fun MedicineItem(modifier: Modifier = Modifier, medicine: Medicine) {
    var showMore by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = "${medicine.startDate} - ${medicine.endDate}",
            style = MaterialTheme.typography.titleLarge
        )
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
            Text(text = "${medicine.name} - ${medicine.type}")
            if (showMore) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = medicine.dosage)
                    Text(text = medicine.frequency)
                }
                Text(text = medicine.prescribedBy)
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