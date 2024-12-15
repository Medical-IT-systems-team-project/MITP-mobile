package org.umcs.mobile.network.dto.case

import kotlinx.datetime.LocalDate
import kotlinx.serialization.*
import org.umcs.mobile.network.dto.serializer.LocalDateSerializer

@Serializable
data class TreatmentResponseDto(
    val name: String,
    @Serializable(with = LocalDateSerializer::class)
    val startDate: LocalDate,
    @Serializable(with = LocalDateSerializer::class)
    val endDate: LocalDate,
    val details : String,
    val medicalDoctorName : String,
    val status : ItemStatus
)


