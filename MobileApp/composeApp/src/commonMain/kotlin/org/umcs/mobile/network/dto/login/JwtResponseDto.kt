package org.umcs.mobile.network.dto.login

import kotlinx.serialization.Serializable

@Serializable
data class JwtResponseDto (
    //TODO : add doctor ID here
    val login: String,
    val token : String
)