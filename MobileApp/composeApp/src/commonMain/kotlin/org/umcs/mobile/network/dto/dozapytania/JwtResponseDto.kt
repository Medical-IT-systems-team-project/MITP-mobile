package org.umcs.mobile.network.dto.dozapytania

import kotlinx.serialization.*

@Serializable
data class JwtResponseDtoDto (
    val login: String,
    val token : String
)