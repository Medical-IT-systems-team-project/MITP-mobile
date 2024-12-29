package org.umcs.mobile.network.dto.case

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import org.umcs.mobile.network.dto.serializer.LocalDateSerializer

@Serializable
data class MedicationResponseDto  (
  val id : Int,
    val name: String,
    @Serializable(with = LocalDateSerializer::class)
    val startDate : LocalDate,
    @Serializable(with = LocalDateSerializer::class)
    val endDate : LocalDate,
    val dosage : String,
    val frequency : String,
    val strength : String,
    val unit : String,
    val medicalDoctorName : String,
    val status : MedicalStatus
)