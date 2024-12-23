package org.umcs.mobile.data

import kotlinx.serialization.Serializable

@Serializable
data class Case(
    val patientName : String,
    val status: CaseStatus,
    val admissionReason : String,
    val admissionDate : String,
    val description : String,
    val createdBy : String,
    val attendingDoctor : String,
)

enum class CaseStatus{
    ONGOING,
    COMPLETED
}