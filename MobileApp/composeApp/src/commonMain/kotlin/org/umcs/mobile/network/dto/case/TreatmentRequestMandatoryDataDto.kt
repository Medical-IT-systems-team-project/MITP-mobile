package org.umcs.mobile.network.dto.case

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.umcs.mobile.network.dto.serializer.LocalDateTimeSerializer

@Serializable
data class TreatmentRequestMandatoryDataDto  (
    val name: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val startDate : LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val endDate : LocalDateTime,
    val status : String
)