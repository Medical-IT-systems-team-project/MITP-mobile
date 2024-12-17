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
import org.umcs.mobile.testhelpers.generateRandomMedicationJson
import org.umcs.mobile.testhelpers.generateRandomTreatmentJson
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DoctorEndpointTest {
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
                        // Only send the Authorization header if the request is not for the login endpoint
                        !request.url.encodedPath.contains("login")
                    }
                }
            }
        }
        getTokens()
    }

    fun getTokens() = runBlocking {
        val tokenResponse: JwtResponseDto = client.post("login") {
            contentType(ContentType.Application.Json)
            setBody(TokenRequestDto(login = "bazinga", password = "bazinga"))
        }.body()
        tokens = BearerTokens(accessToken = tokenResponse.token, refreshToken = null)
    }

    @Test
    fun `get all patients as a doctor should return 200-OK`() = runTest {
        val allPatientsResponse = client.get("doctor/patient/all")
        assertEquals(HttpStatusCode.OK, allPatientsResponse.status)
    }

    @Test
    fun `create new treatment should return 201-Created with medicalCaseId and medicalDoctorId equal 1 and random data `() = runTest {
        val randomTreatment = generateRandomTreatmentJson()
        val randomTreatmentResponse = client.post("doctor/new/Treatment"){
            setBody(randomTreatment)
        }
        println(randomTreatment)
        println(randomTreatmentResponse.bodyAsText())
        assertEquals(HttpStatusCode.Created,randomTreatmentResponse.status)
    }

    @Test
    fun `create new medication should return 201-Created  medicalCaseId and medicalDoctorId equal 1 and random data`() = runTest {
        val randomMedication = generateRandomMedicationJson()
        val randomTreatmentResponse = client.post("doctor/new/Medication"){
            setBody(randomMedication)
        }
        println(randomMedication)
        println(randomTreatmentResponse.bodyAsText())
        assertEquals(HttpStatusCode.Created,randomTreatmentResponse.status)
    }

    //TODO : create tests for status endpoints
}