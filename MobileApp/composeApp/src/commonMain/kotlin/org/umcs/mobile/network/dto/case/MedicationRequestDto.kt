package org.umcs.mobile.network.dto.case

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import org.umcs.mobile.network.dto.serializer.LocalDateSerializer

@Serializable
data class MedicationRequestDto (
    val name: String,
    @Serializable(with = LocalDateSerializer::class)
    val startDate : LocalDate,
    @Serializable(with = LocalDateSerializer::class)
    val endDate : LocalDate,
    val details : String,
    val medicalCaseId : Int,
    val medicalDoctorId : String,
    val dosageForm : String,
    val strength : String,
    val unit : String
)