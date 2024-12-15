package org.umcs.mobile.network

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
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
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.umcs.mobile.network.dto.dozapytania.JwtResponseDto
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
        install(Auth) {
            bearer {
                loadTokens {
                    tokens
                }
                sendWithoutRequest { request ->
                    // Only send the Authorization header if the request is not for the login endpoint
                    !request.url.encodedPath.contains("login")
                }
            }
        }
    }

    fun initClient(){
        scope.launch{
            loginAndGetTokens()
        }
    }

    suspend fun loginAndGetTokens(){
        val tokenResponse: JwtResponseDto = client.post("login") {
            contentType(ContentType.Application.Json)
            setBody(TokenRequestDto(login = "bazinga", password = "bazinga"))
        }.body()
        Logger.i("TOKEN : $tokenResponse", tag = "Ktor")

        tokens = BearerTokens(accessToken = tokenResponse.token, refreshToken = null)
    }

    suspend fun testNewPatient(){
        val testPatient = """
            {
              "socialSecurityNumber": "10041173121",
              "firstName": "strisng",
              "lastName": "strsing",
              "age": 2,
              "gender": "string",
              "address": "string",
              "phoneNumber": "361660134",
              "email": "gG3XTcr2u3.oiI6M054_WeoLRnhiJPbgGW%6ip6.@Z1YpMh2mJZnaFMJEoDiu-mNAhav8dhKZlaeCE6rudCAcW4TUaxQAg.nxWuRyEteYDcOGDPHVSDqgGcNPEWHdZXWnKhsFdItxIvs",
              "birthDate": "2024-12-15T14:27:01.075Z"
            }
        """
        val response = client.post("patient/new"){
            setBody(testPatient)
        }

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