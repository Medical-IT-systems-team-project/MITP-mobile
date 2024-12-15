package org.umcs.mobile.network.dto.case

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import org.umcs.mobile.network.dto.serializer.LocalDateSerializer

@Serializable
data class MedicalCaseRequestDto(
    val patientId : Int,
    val admissionReason:String,
    @Serializable(with = LocalDateSerializer::class)
    val admissionDate:LocalDate,
    val description : String,
    val attendingDoctorId:Int
)
