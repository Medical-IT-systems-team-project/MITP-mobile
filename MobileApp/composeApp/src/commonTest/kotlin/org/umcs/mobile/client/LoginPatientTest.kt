package org.umcs.mobile.client

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.umcs.mobile.network.Endpoints
import org.umcs.mobile.network.Endpoints.withArgs
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class LoginPatientTest {
    private lateinit var tokens: BearerTokens
    private lateinit var client: HttpClient

    @BeforeTest
    fun setup() {
        client = HttpClient {
            defaultRequest {
                contentType(ContentType.Application.Json)
                url(baseUrl)
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
                        val path = request.url.encodedPath
                        !path.contains("login") &&
                                !path.contains("register") &&
                                !(path.contains("patient/accessID") && path != "patient/new") &&
                                !path.contains("history") &&
                                !(path.startsWith("patient/") && path != "patient/new")                    }
                }
            }
        }
    }

    @Test
    fun `login as patient should return 200-OK with accessID =A12345`() = runTest {
        val accessID = "A123451234"
        val patientResponse = client.get(Endpoints.PATIENT_ACCESS_ID.withArgs(accessID))

        assertEquals(HttpStatusCode.OK, patientResponse.status)
    }

    @Test
    fun `get medical history for patient should return 200-OK with accessID =A12345`() = runTest {
        val accessID = "A123451234"
        val medicalCasesResponses = client.get(Endpoints.MEDICAL_CASE_ACCESS_ID_HISTORY.withArgs(accessID))
        Logger.i(medicalCasesResponses.bodyAsText())
        assertEquals(HttpStatusCode.OK, medicalCasesResponses.status)
    }
}