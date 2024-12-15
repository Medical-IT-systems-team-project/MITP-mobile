package org.umcs.mobile.network.dto.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class PhoneNumberPatternSerializer : KSerializer<String> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Pattern", PrimitiveKind.STRING)
    private val phoneNumberPattern = "^[0-9]{9}\$".toRegex()

    override fun deserialize(decoder: Decoder): String {
        val value = decoder.decodeString()
        if(!phoneNumberPattern.matches(value)){
            throw IllegalArgumentException("Value didnt match phone number pattern during deserialization")
        }

        return value
    }

    override fun serialize(encoder: Encoder, value: String) {
        if (!phoneNumberPattern.matches(value)) {
            throw IllegalArgumentException("Value didnt match phone number pattern during serialization")
        }
        encoder.encodeString(value)
    }
}
