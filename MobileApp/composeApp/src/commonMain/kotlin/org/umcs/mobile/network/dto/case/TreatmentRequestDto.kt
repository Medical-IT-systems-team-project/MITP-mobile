package org.umcs.mobile.network.dto.case

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.umcs.mobile.network.dto.serializer.LocalDateTimeSerializer

@Serializable
data class TreatmentRequestDto(
    val description: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val startDate: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val endDate: LocalDateTime,
    val name: String,
    val details: String,
    val medicalCaseId: Int,
    val medicalDoctorId: String,
)