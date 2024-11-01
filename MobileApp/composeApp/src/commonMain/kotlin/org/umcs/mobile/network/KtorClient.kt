package org.umcs.mobile.network

import io.ktor.client.HttpClient
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

expect object KtorClient {
    val client : HttpClient
    suspend fun get(url: String): HttpResponse
    suspend fun post(url: String, body: Any): HttpResponse
}

@Serializable
data class Welcome (
    val data: Data,
    val support: Support
)

@Serializable
data class Data (
    val id: Long,
    val email: String,

    @SerialName("first_name")
    val firstName: String,

    @SerialName("last_name")
    val lastName: String,

    val avatar: String
)

@Serializable
data class Support (
    val url: String,
    val text: String
)
