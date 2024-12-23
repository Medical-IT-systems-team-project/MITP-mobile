package org.umcs.mobile.network.dto.case

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import org.umcs.mobile.data.CaseStatus

@Serializable
data class MedicalCaseResponseDto  (
    val status: CaseStatus,
    val admissionReason : String,
    val admissionDate : LocalDate,
    val description : String,
    val createdBy : String,
    val attendingDoctor : String,
    val medications : List<MedicationResponseDto>,
    val treatments : List<TreatmentResponseDto>,
    val allowedDoctors : List<String>
)

