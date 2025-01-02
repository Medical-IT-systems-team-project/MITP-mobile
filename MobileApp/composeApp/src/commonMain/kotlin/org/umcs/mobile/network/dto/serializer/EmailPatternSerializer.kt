package org.umcs.mobile.network.dto.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class EmailPatternSerializer : KSerializer<String> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Pattern", PrimitiveKind.STRING)
    private val emailNumbersPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$".toRegex()

    override fun deserialize(decoder: Decoder): String {
        val value = decoder.decodeString()
        if(!emailNumbersPattern.matches(value)){
            throw IllegalArgumentException("Value didnt match email pattern during deserialization")
        }

        return value
    }

    override fun serialize(encoder: Encoder, value: String) {
        if (!emailNumbersPattern.matches(value)) {
            throw IllegalArgumentException("Value didnt match email pattern during serialization")
        }
        encoder.encodeString(value)
    }
}
