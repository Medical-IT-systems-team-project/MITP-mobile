package org.umcs.mobile.network.dto.patient

import kotlinx.datetime.LocalDate
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.umcs.mobile.network.dto.serializer.EmailPatternSerializer
import org.umcs.mobile.network.dto.serializer.LocalDateSerializer
import org.umcs.mobile.network.dto.serializer.PhoneNumberPatternSerializer
import org.umcs.mobile.network.dto.serializer.SSNPatternSerializer

@Serializable
data class PatientRequestDto (
    @Serializable(with = SSNPatternSerializer::class)
    val socialSecurityNumber: String,
    val firstName : String,
    val lastname:String,
    val age : Int,
    val gender:String,
    val address : String,
    @Serializable(with = PhoneNumberPatternSerializer::class)
    val phoneNumber :String,
    @Serializable(with = EmailPatternSerializer::class)
    val email : String,
    @Serializable(with = LocalDateSerializer::class)
    val birthDate: LocalDate //TODO : force format dd/mm/yyyy or something similar
)