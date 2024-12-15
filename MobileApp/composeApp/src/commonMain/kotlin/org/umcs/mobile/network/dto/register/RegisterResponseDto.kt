package org.umcs.mobile.network.dto.register

import kotlinx.serialization.*

@Serializable
data class RegisterResponseDtoDto (
    val login: String,
    val message : String
)