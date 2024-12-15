package org.umcs.mobile.network.dto.dozapytania

import kotlinx.serialization.*

@Serializable
data class TokenRequestDto (
    val login: String,
    val password : String
)