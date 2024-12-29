package org.umcs.mobile.data

import kotlinx.serialization.Serializable
import org.umcs.mobile.composables.case_view.Medication
import org.umcs.mobile.composables.case_view.Treatment

@Serializable
data class Case(
    val patientName: String,
    val status: CaseStatus,
    val admissionReason: String,
    val admissionDate: String,
    val description: String,
    val createdBy: String,
    val attendingDoctor: String,
    val medications: List<Medication>,
    val treatments: List<Treatment>,
    val allowedDoctors: List<String>,
)


enum class CaseStatus {
    ONGOING,
    COMPLETED
}