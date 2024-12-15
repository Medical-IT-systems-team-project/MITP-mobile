package org.umcs.mobile.network

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.authProviders
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.umcs.mobile.network.dto.dozapytania.JwtResponseDtoDto
import org.umcs.mobile.network.dto.dozapytania.TokenRequestDto

object GlobalKtorClient {
     private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private lateinit var tokens : BearerTokens
    private val client = HttpClient {
        defaultRequest {
            contentType(ContentType.Application.Json)
            url("https://caretrack.skni.umcs.pl/")
        }
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
        install(Logging) {
            logger = object : io.ktor.client.plugins.logging.Logger {
                override fun log(message: String) {
                    Logger.v(message, tag = "Ktor")
                }
            }
            level = LogLevel.ALL
        }
    }
    init {
        scope.launch{
            loginAndGetTokens()
        }
    }

    suspend fun loginAndGetTokens(){
        val tokenResponse: JwtResponseDtoDto = client.post("login") {
            contentType(ContentType.Application.Json)
            setBody(TokenRequestDto(login = "bazinga", password = "bazinga"))
        }.body()
        Logger.i("TOKEN : $tokenResponse", tag = "Ktor")

        tokens = BearerTokens(accessToken = tokenResponse.token, refreshToken = null)
    }

    /*  suspend fun registerTest() {
          val testRegisterDoctorData = """{
              "login": "bazinga",
              "password": "bazinga"
          }"""
          val response = client.post("register"){
              setBody(testRegisterDoctorData)
          }
      }*/


    suspend fun getRandomJson(): String {
        val response = client.get("https://reqres.in/api/users/2")
        return response.bodyAsText()
    }
}