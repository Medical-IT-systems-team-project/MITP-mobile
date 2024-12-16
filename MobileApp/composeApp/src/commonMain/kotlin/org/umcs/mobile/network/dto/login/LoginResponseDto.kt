package org.umcs.mobile.network.dto.login

import kotlinx.serialization.*

@Serializable
data class LoginResponseDto(
    val login: String,
    val password : String
)