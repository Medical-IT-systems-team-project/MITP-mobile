package org.umcs.mobile.network.dto.login

import kotlinx.serialization.Serializable

@Serializable
data class JwtResponseDto (
    val login: String,
    val token : String,
    val id : String
)