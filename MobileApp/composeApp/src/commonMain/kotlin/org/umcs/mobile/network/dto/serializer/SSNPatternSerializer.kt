package org.umcs.mobile.network.dto.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class SSNPatternSerializer : KSerializer<String> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Pattern", PrimitiveKind.STRING)
    private val elevenNumbersPattern = "^\\d{11}\$".toRegex()

    override fun deserialize(decoder: Decoder): String {
        val value = decoder.decodeString()
        if(!elevenNumbersPattern.matches(value)){
            throw IllegalArgumentException("Value didnt match SSN pattern during deserialization")
        }

        return value
    }

    override fun serialize(encoder: Encoder, value: String) {
        if (!elevenNumbersPattern.matches(value)) {
            throw IllegalArgumentException("Value didnt match SSN pattern during serialization")
        }
        encoder.encodeString(value)
    }
}