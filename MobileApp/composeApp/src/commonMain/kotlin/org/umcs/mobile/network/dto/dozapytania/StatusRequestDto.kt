package org.umcs.mobile.network.dto.dozapytania

import kotlinx.serialization.*

@Serializable
data class StatusRequestDto (
    val status: String,
)