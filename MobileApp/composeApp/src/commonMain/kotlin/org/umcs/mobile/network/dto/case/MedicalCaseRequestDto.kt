package org.umcs.mobile.network.dto.case

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.umcs.mobile.network.dto.serializer.LocalDateTimeSerializer

@Serializable
data class MedicalCaseRequestDto(
    val patientId : String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val admissionDate: LocalDateTime,
    val admissionReason:String,
    val description : String,
    val attendingDoctorId:String
)
