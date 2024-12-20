package org.umcs.mobile.network.dto.dozapytania

import kotlinx.serialization.Serializable
import org.umcs.mobile.network.dto.case.MedicalStatus

@Serializable
data class StatusRequestDto (
    val status: MedicalStatus,
)