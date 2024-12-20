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
import io.ktor.client.request.patch
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
import org.umcs.mobile.network.dto.case.MedicalStatus
import org.umcs.mobile.network.dto.dozapytania.StatusRequestDto
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
                        !request.url.encodedPath.contains("login") &&
                                !request.url.encodedPath.contains("register")
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
    fun `get all patients as a doctor should return 200-OK`() = runTest {
        val allPatientsResponse = client.get(Endpoints.DOCTOR_PATIENT_ALL)
        assertEquals(HttpStatusCode.OK, allPatientsResponse.status)
    }

    @Test
    fun `get all cases as a doctor should return 200-OK`() = runTest {
        val allPatientsResponse = client.get(Endpoints.DOCTOR_MEDICAL_CASE_ALL)
        assertEquals(HttpStatusCode.OK, allPatientsResponse.status)
    }

    @Test
    fun `create new treatment should return 201-Created with medicalCaseId and medicalDoctorId equal 1 and random data `() =
        runTest {
            val randomTreatment = generateRandomTreatmentJson()
            val randomTreatmentResponse = client.post(Endpoints.DOCTOR_NEW_TREATMENT) {
                setBody(randomTreatment)
            }
            println(randomTreatment)
            println(randomTreatmentResponse.bodyAsText())
            assertEquals(HttpStatusCode.Created, randomTreatmentResponse.status)
        }

    @Test
    fun `create new medication should return 201-Created  medicalCaseId and medicalDoctorId equal 1 and random data`() =
        runTest {
            val randomMedication = generateRandomMedicationJson()
            val randomTreatmentResponse = client.post(Endpoints.DOCTOR_NEW_MEDICATION) {
                setBody(randomMedication)
            }
            println(randomMedication)
            println(randomTreatmentResponse.bodyAsText())
            assertEquals(HttpStatusCode.Created, randomTreatmentResponse.status)
        }

    @Test
    fun `change treatment status should return 200-OK with ID = 1 and status = PLANNED`()=
        runTest {
            val status = StatusRequestDto(MedicalStatus.PLANNED)
            println(status)
            val treatmentStatusChangeResponse= client.patch(Endpoints.DOCTOR_TREATMENT_ID_STATUS.withArgs("1")) {
                setBody(status)
            }
            println(treatmentStatusChangeResponse.bodyAsText())
            assertEquals(HttpStatusCode.OK, treatmentStatusChangeResponse.status)
        }

    @Test
    fun `change medication status should return 200-OK with ID = 1 and status = PLANNED`()=
        runTest {
            val status = StatusRequestDto(MedicalStatus.PLANNED)
            val medicationStatusChangeResponse= client.patch(Endpoints.DOCTOR_MEDICATION_ID_STATUS.withArgs("1")) {
                setBody(status)
            }
            println(medicationStatusChangeResponse.bodyAsText())
            assertEquals(HttpStatusCode.OK, medicationStatusChangeResponse.status)
        }
}