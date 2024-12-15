package org.umcs.mobile.network.dto.register

import kotlinx.serialization.*

@Serializable
data class RegisterRequestDto (
    val login: String,
    val password : String,
)