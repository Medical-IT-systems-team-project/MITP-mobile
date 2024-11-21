package org.umcs.mobile.composables.case_view

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
fun TreatmentsContent(modifier: Modifier = Modifier, paddingValues: PaddingValues, case: Case) {
    val treatments = remember { fetchTreatment() }

    LazyColumn(
        contentPadding = paddingValues,
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start
    ) {
        items(treatments) { treatment ->
            TreatmentItem(treatment = treatment)
        }
    }
}

@Composable
fun TreatmentItem(modifier: Modifier = Modifier, treatment: Treatment) {
    var showMore by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = "${treatment.startDate} - ${treatment.endDate}",
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
            Text(text = treatment.details)
            if (showMore) {
                Text(text = treatment.doctorName)
                Text(text = treatment.description)
            }
        }
    }
}


data class Treatment(
    val startDate: String,
    val endDate: String,
    val details: String,
    val description: String,
    val doctorName: String,
)

fun fetchTreatment() = listOf(
    Treatment(
        startDate = "2024-01-01",
        endDate = "2024-01-15",
        details = "Prescribed amoxicillin 500mg twice daily",
        description = "Antibiotic treatment for respiratory infection",
        doctorName = "Dr. Smith"
    ),
    Treatment(
        startDate = "2024-02-01",
        endDate = "2024-02-28",
        details = "Strengthening exercises and mobility training",
        description = "Physical therapy for knee rehabilitation",
        doctorName = "Dr. Johnson"
    ),
    Treatment(
        startDate = "2024-03-10",
        endDate = "2024-03-25",
        details = "Increased dosage of blood pressure medication",
        description = "Hypertension medication adjustment",
        doctorName = "Dr. Williams"
    ),
    Treatment(
        startDate = "2024-04-05",
        endDate = "2024-04-20",
        details = "Weekly therapy sessions focusing on anxiety management",
        description = "Cognitive behavioral therapy sessions",
        doctorName = "Dr. Brown"
    ),
    Treatment(
        startDate = "2024-05-01",
        endDate = "2024-05-15",
        details = "Daily physical exercises and wound care",
        description = "Post-surgery rehabilitation program",
        doctorName = "Dr. Davis"
    )
)