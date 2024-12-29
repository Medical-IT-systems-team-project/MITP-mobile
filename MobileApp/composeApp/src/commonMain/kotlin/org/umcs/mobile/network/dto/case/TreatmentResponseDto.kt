package org.umcs.mobile.network.dto.case

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.umcs.mobile.network.dto.serializer.LocalDateTimeSerializer

@Serializable
data class TreatmentResponseDto(
   // val treatmentId : Int,
    val name: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val startDate: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val endDate: LocalDateTime,
    val details : String,
    val medicalDoctorName : String,
    val status : MedicalStatus
)


