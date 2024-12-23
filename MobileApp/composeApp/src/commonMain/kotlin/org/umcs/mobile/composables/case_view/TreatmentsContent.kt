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
import org.umcs.mobile.data.Case
import org.umcs.mobile.network.dto.case.MedicalStatus
import org.umcs.mobile.theme.determineTheme

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
    val style = when (determineTheme()) {
        Cupertino -> CupertinoTheme.typography.subhead
        Material3 -> MaterialTheme.typography.bodyMedium
    }

    Column(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionTitleText(treatment.name)
            StatusIcon(treatment.status)
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


data class Treatment(
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
        status = MedicalStatus.PLANNED
    ),
    Treatment(
        name = "Physical Therapy",
        startDate = "2024-02-01",
        endDate = "2024-02-28",
        details = "Strengthening exercises and mobility training",
        createdBy = "Dr. Johnson",
        status = MedicalStatus.ONGOING
    ),
    Treatment(
        name = "Blood Pressure Management",
        startDate = "2024-03-10",
        endDate = "2024-03-25",
        details = "Increased dosage of blood pressure medication",
        createdBy = "Dr. Williams",
        status = MedicalStatus.PLANNED
    ),
    Treatment(
        name = "Therapy Sessions",
        startDate = "2024-04-05",
        endDate = "2024-04-20",
        details = "Weekly therapy sessions focusing on anxiety management",
        createdBy = "Dr. Brown",
        status = MedicalStatus.CANCELLED
    ),
    Treatment(
        name = "Post-Surgery Care",
        startDate = "2024-05-01",
        endDate = "2024-05-15",
        details = "Daily physical exercises and wound care",
        createdBy = "Dr. Davis",
        status = MedicalStatus.COMPLETED
    )
)