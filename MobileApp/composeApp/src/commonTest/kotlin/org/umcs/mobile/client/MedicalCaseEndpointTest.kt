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
import io.ktor.client.statement.HttpResponse
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
import org.umcs.mobile.testhelpers.generateRandomPatientJson
import org.umcs.mobile.testhelpers.randomLetters
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MedicalCaseEndpointTest {
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
                        !request.url.encodedPath.contains("login") &&
                                !request.url.encodedPath.contains("patient/new") &&
                                !request.url.encodedPath.contains("register")


                    }
                }
            }
        }
        registerDoctor()
        registerPatient()
        getTokens()
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
        val tokenResponse = client.post("login") {
            contentType(ContentType.Application.Json)
            setBody(TokenRequestDto(login = "test", password = "test"))
        }
        println(tokenResponse.status)
        val tokenResponseDto : JwtResponseDto = tokenResponse.body()
        tokens = BearerTokens(accessToken = tokenResponseDto.token, refreshToken = null)
    }

    @Test
    fun `create new case should return 201-Created with any data`() = runTest {
        val response: HttpResponse = client.post(Endpoints.MEDICAL_CASE_NEW) {
            setBody(
                """
                {
                 "patientId": 1,
                 "admissionReason": "bazinga",
                 "admissionDate": "2024-12-31T05:30",
                 "description": "testowe przyjecie",
                 "attendingDoctorId": 1
                }
                """
            )
        }
        println("error = ${response.bodyAsText()}")
        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun `get medical case treatments should return 200-OK with ID equal 1`() = runTest {
        val response = client.get(Endpoints.MEDICAL_CASE_ID_TREATMENT_ALL.withArgs("1"))
        assertEquals(HttpStatusCode.OK , response.status)
    }

    @Test
    fun `get medical case treatments should return 404-NotFound with ID equal 0`() = runTest {
        val response = client.get(Endpoints.MEDICAL_CASE_ID_TREATMENT_ALL.withArgs("0"))
        assertEquals(HttpStatusCode.NotFound , response.status)
    }

    @Test
    fun `get medical case medications should return 200-OK with ID equal 1`() = runTest {
        val response = client.get(Endpoints.MEDICAL_CASE_ID_MEDICATION_ALL.withArgs("1"))
        assertEquals(HttpStatusCode.OK , response.status)
    }

    @Test
    fun `get medical case medications should return 404-NotFound with ID equal 0`() = runTest {
        val response = client.get(Endpoints.MEDICAL_CASE_ID_MEDICATION_ALL.withArgs("0"))
        assertEquals(HttpStatusCode.NotFound , response.status)
    }

    @Test
    fun `get medical case summary should return 200-OK with new patient's access ID`() = runTest {
        val testPatient = generateRandomPatientJson()
        val newPatientResponse = client.post(Endpoints.PATIENT_NEW) {
            setBody(testPatient)
        }
        val accessID = newPatientResponse.bodyAsText()
        val getNewPatientsSummaryResponse = client.get(Endpoints.MEDICAL_CASE_ACCESS_ID_SUMMARY.withArgs(accessID))
        assertEquals(HttpStatusCode.OK, getNewPatientsSummaryResponse.status)
    }

    @Test
    fun `get medical case summary should return 404-NotFound with non-existing accessID`() = runTest {
        val accessID = randomLetters(10)
        val getPatientsSummaryResponse = client.get(Endpoints.MEDICAL_CASE_ACCESS_ID_SUMMARY.withArgs(accessID))
        assertEquals(HttpStatusCode.NotFound, getPatientsSummaryResponse.status)
    }
    //TODO : W jakim wypadku tutaj zachodzi niby 400
}