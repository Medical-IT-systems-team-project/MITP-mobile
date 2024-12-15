package org.umcs.mobile.network.dto.patient

import kotlinx.datetime.LocalDate
import kotlinx.serialization.*
import org.umcs.mobile.network.dto.serializer.EmailPatternSerializer
import org.umcs.mobile.network.dto.serializer.LocalDateSerializer
import org.umcs.mobile.network.dto.serializer.PhoneNumberPatternSerializer
import org.umcs.mobile.network.dto.serializer.SSNPatternSerializer

@Serializable
data class PatientResponseDto(
    val firstName: String,
    val lastName: String,
    @Serializable(with = LocalDateSerializer::class)
    val birthDate: LocalDate,
    @Serializable(with = SSNPatternSerializer::class)
    val socialSecurityNumber: String,
    val age: String,
    val gender: String,
    val accessId: String,
    @Serializable(with = EmailPatternSerializer::class)
    val email: String,
    @Serializable(with = PhoneNumberPatternSerializer::class)
    val phoneNumber: String,
    val address: String
)