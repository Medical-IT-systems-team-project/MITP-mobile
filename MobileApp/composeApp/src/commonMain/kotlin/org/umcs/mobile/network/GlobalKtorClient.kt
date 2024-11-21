package org.umcs.mobile.network

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

object GlobalKtorClient {
    private val client = HttpClient()

    suspend fun getRandomJson(): String {
        val response = client.get("https://reqres.in/api/users/2")
        return response.bodyAsText()
    }
}