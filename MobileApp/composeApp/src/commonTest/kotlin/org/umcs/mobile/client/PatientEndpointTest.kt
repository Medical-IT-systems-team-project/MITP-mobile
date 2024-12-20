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
import org.umcs.mobile.network.Endpoints
import org.umcs.mobile.network.Endpoints.withArgs
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
                                !request.url.encodedPath.contains("register") &&
                                !(request.url.encodedPath.contains("patient/") && request.url.encodedPath.equals("patient/new"))
                    }
                }
            }
        }
        registerDoctor()
        getTokens()
        registerPatient()
    }

    private fun registerPatient() = runBlocking {
        val registerPatient = client.post(Endpoints.PATIENT_NEW) {
            setBody(
                """
                    {
                      "socialSecurityNumber": "61145564327",
                      "firstName": "string",
                      "lastName": "string",
                      "age": 12,
                      "gender": "MALE",
                      "address": "string",
                      "phoneNumber": "553724316",
                      "email": "mihas@gmail.com",
                      "birthDate": "2024-12-20"
                    }
                """
            )
        }
        println("Register patient returned with status ${registerPatient.status}")
    }

    private fun registerDoctor() = runBlocking {
        val registerResponse = client.post(Endpoints.REGISTER) {
            setBody(
                """
                    {
                     "login": "test",
                     "password": "test"
                    }
                """
            )
        }
        println("Register doctor returned with status ${registerResponse.status}")
    }

    private fun getTokens() = runBlocking {
        val tokenResponse: JwtResponseDto = client.post("login") {
            setBody(TokenRequestDto(login = "test", password = "test"))
        }.body()
        println("Logowanie $tokenResponse")
        tokens = BearerTokens(accessToken = tokenResponse.token, refreshToken = null)
    }

    @Test
    fun `create new patient should return error with randomized data fitting the schema`() = runTest {
        val testPatient = generateErrorRandomPatientJson()
        val newPatientResponse = client.post(Endpoints.PATIENT_NEW) {
            setBody(testPatient)
        }
        println(testPatient)
        println(newPatientResponse.bodyAsText())
        assertEquals(HttpStatusCode.Unauthorized, newPatientResponse.status)
    }

    @Test
    fun `create new patient should return 200-OK with randomized data fitting the schema`() = runTest {
        val testPatient = generateRandomPatientJson()
        val newPatientResponse = client.post(Endpoints.PATIENT_NEW) {
            setBody(testPatient)
        }
        println(testPatient)
        println(newPatientResponse.bodyAsText())
        assertEquals(HttpStatusCode.OK, newPatientResponse.status)
    }

    @Test
    fun `get new patient should return 200-OK`() = runTest {
        val testPatient = generateRandomPatientJson()
        val newPatientResponse = client.post(Endpoints.PATIENT_NEW) {
            setBody(testPatient)
        }
        val accessID = newPatientResponse.bodyAsText()
        val getNewPatientResponse = client.get(Endpoints.PATIENT_ACCESS_ID.withArgs(accessID))
        println(getNewPatientResponse.bodyAsText())
        assertEquals(HttpStatusCode.OK, getNewPatientResponse.status)
    }
}