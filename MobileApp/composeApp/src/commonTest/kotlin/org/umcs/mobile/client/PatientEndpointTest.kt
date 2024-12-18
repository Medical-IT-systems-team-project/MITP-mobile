package org.umcs.mobile.client

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.umcs.mobile.network.dto.login.JwtResponseDto
import org.umcs.mobile.network.dto.login.TokenRequestDto
import org.umcs.mobile.testhelpers.generateErrorRandomPatientJson
import org.umcs.mobile.testhelpers.generateRandomPatientJson
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class PatientEndpointTest {
    private lateinit var tokens: BearerTokens
    private lateinit var client: HttpClient

    @BeforeTest
    fun setup() {
        client = HttpClient {
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
                level = LogLevel.BODY
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        tokens
                    }
                    sendWithoutRequest { request ->
                        !request.url.encodedPath.contains("login") &&
                                !(request.url.encodedPath.contains("patient/") && !request.url.encodedPath.equals("patient/new"))
                    }
                }
            }
        }
        getTokens()
    }

    fun getTokens() = runBlocking {
        val tokenResponse: JwtResponseDto = client.post("login") {
            contentType(ContentType.Application.Json)
            setBody(TokenRequestDto(login = "test", password = "test"))
        }.body()
        tokens = BearerTokens(accessToken = tokenResponse.token, refreshToken = null)
    }


    @Test
    fun `create new patient should return error with randomized data fitting the schema`() = runTest {
        val testPatient = generateErrorRandomPatientJson()
        val newPatientResponse = client.post("patient/new") {
            setBody(testPatient)
        }
        println(testPatient)
        println(newPatientResponse.bodyAsText())
        assertEquals(HttpStatusCode.BadRequest, newPatientResponse.status)
    }

    @Test
    fun `create new patient should return 200-OK with randomized data fitting the schema`() = runTest {
        val testPatient = generateRandomPatientJson()
        val newPatientResponse = client.post("patient/new") {
            setBody(testPatient)
        }
        println(testPatient)
        println(newPatientResponse.bodyAsText())
        assertEquals(HttpStatusCode.OK, newPatientResponse.status)
    }

    @Test
    fun `get new patient should return 200-OK`() = runTest {
        val testPatient = generateRandomPatientJson()
        val newPatientResponse = client.post("patient/new") {
            setBody(testPatient)
        }
        val accessID = newPatientResponse.bodyAsText()
        val getNewPatientResponse = client.get("patient/$accessID")
        println(getNewPatientResponse.bodyAsText())
        assertEquals(HttpStatusCode.OK, getNewPatientResponse.status)
    }


}