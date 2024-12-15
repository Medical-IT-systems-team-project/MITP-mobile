package org.umcs.mobile.network.dto.dozapytania

import kotlinx.serialization.*

@Serializable
data class JwtResponseDto (
    val login: String,
    val token : String
)