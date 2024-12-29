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
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.Json
import org.umcs.mobile.composables.new_case_view.MedicalCase
import org.umcs.mobile.composables.new_medication_view.Medication
import org.umcs.mobile.composables.new_treatment_view.MedicalTreatment
import org.umcs.mobile.network.Endpoints.withArgs
import org.umcs.mobile.network.dto.case.MedicalCaseRequestDto
import org.umcs.mobile.network.dto.case.MedicalCaseResponseDto
import org.umcs.mobile.network.dto.case.MedicalStatus
import org.umcs.mobile.network.dto.case.MedicationRequestDto
import org.umcs.mobile.network.dto.case.TreatmentRequestDto
import org.umcs.mobile.network.dto.dozapytania.StatusRequestDto
import org.umcs.mobile.network.dto.login.JwtResponseDto
import org.umcs.mobile.network.dto.login.TokenRequestDto
import org.umcs.mobile.network.dto.patient.PatientResponseDto

const val baseUrl = "https://caretrack.skni.umcs.pl/"

object GlobalKtorClient {
    private lateinit var tokens: BearerTokens
    private val client = HttpClient {
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
                    val path = request.url.encodedPath
                    !path.contains("login") &&
                            !path.contains("register") &&
                            !(path.contains("patient/accessID") && path != "patient/new") &&
                            !path.contains("history") &&
                            !(path.startsWith("patient/") && path != "patient/new")
                }
            }
        }
    }

    suspend fun loginAsDoctor(login: String, password: String): DoctorLoginResult {
        return try {
            val doctorResponse = client.post(Endpoints.LOGIN) {
                setBody(TokenRequestDto(login, password))
            }
            Logger.i("TOKEN : ${doctorResponse.bodyAsText()}", tag = "Ktor")

            when (doctorResponse.status) {
                HttpStatusCode.OK -> {
                    val loginBody: JwtResponseDto = doctorResponse.body()
                    tokens = BearerTokens(accessToken = loginBody.token, refreshToken = null)
                    DoctorLoginResult.Success(loginBody)
                }

                HttpStatusCode.NotFound -> {
                    throw NoSuchElementException("No doctor found with that data")
                }

                else -> {
                    throw IllegalStateException("${doctorResponse.status} with message ${doctorResponse.bodyAsText()}  \uD83E\uDD21 ")
                }
            }
        } catch (e: Exception) {
            Logger.e("Login failed: ${e.message}", tag = "Ktor")
            DoctorLoginResult.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun createNewCase(newCase: MedicalCase, doctorId: String) {
        return try {
            val medicalCaseRequestDto = MedicalCaseRequestDto(
                patientId = 1, //TODO : change patient data class to hold id
                admissionDate = LocalDateTime.parse(newCase.admissionDate),
                admissionReason = newCase.admissionReason,
                description = newCase.description,
                attendingDoctorId = doctorId
            )
            val medicalCaseResponse = client.post(Endpoints.MEDICAL_CASE_NEW) {
                setBody(medicalCaseRequestDto)
            }
            Logger.v(medicalCaseResponse.status.toString(), tag = "new case")
        } catch (e: Exception) {
            Logger.v(e.message.toString(), tag = "new case")
        }
    }

    suspend fun loginAsPatient(accessID: String): PatientLoginResult {
        return try {
            val patientResponse = client.get(Endpoints.PATIENT_ACCESS_ID.withArgs(accessID))
            when (patientResponse.status) {
                HttpStatusCode.OK -> {
                    val patientDto: PatientResponseDto = patientResponse.body()
                    PatientLoginResult.Success(patientDto)
                }

                HttpStatusCode.NotFound -> {
                    throw NoSuchElementException("No patient found with that access ID")
                }

                else -> {
                    throw IllegalStateException("${patientResponse.status}  \uD83E\uDD21 ")
                }
            }
        } catch (e: Exception) {
            Logger.e("Login failed: ${e.message}", tag = "Ktor")
            PatientLoginResult.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun getAllMedicalCasesAsPatient(accessID: String): AllMedicalCasesResult {
        return try {
            val medicalCasesResponse =
                client.get(Endpoints.MEDICAL_CASE_ACCESS_ID_HISTORY.withArgs(accessID))
            val allMedicalCases: List<MedicalCaseResponseDto> = medicalCasesResponse.body()
            AllMedicalCasesResult.Success(allMedicalCases)
        } catch (e: Exception) {
            AllMedicalCasesResult.Error(e.message.toString())
        }
    }

    suspend fun createNewTreatment(
        newTreatment: MedicalTreatment,
        doctorID: String,
        medicalCaseID: Int,
    ) {
        return try {
            val treatmentRequestDto = TreatmentRequestDto(
                description = newTreatment.description,
                startDate = LocalDateTime.parse(newTreatment.startDate),
                endDate = LocalDateTime.parse(newTreatment.endDate),
                details = newTreatment.details,
                name = newTreatment.name,
                medicalDoctorId = doctorID,
                medicalCaseId = medicalCaseID
            )
            val treatmentResponse = client.post(Endpoints.DOCTOR_NEW_TREATMENT) {
                setBody(treatmentRequestDto)
            }
            Logger.v(treatmentResponse.status.toString(), tag = "new case")
        } catch (e: Exception) {
            Logger.v(e.message.toString(), tag = "new case")
        }
    }

    suspend fun createNewMedication(
        newMedication: Medication,
        doctorID: String,
        medicalCaseID: Int,
    ) {
        return try {
            val treatmentRequestDto = MedicationRequestDto(
                startDate = LocalDate.parse(newMedication.startDate),
                endDate = LocalDate.parse(newMedication.endDate),
                details = newMedication.details,
                name = newMedication.name,
                medicalDoctorId = doctorID,
                medicalCaseId = medicalCaseID,
                dosageForm = newMedication.dosageForm,
                strength = newMedication.strength,
                unit = newMedication.unit
            )
            val treatmentResponse = client.post(Endpoints.DOCTOR_NEW_MEDICATION) {
                setBody(treatmentRequestDto)
            }
            Logger.v(treatmentResponse.status.toString(), tag = "new case")
            Logger.v(treatmentResponse.bodyAsText(), tag = "new case")
        } catch (e: Exception) {
            Logger.v(e.message.toString(), tag = "new case")
        }
    }

    suspend fun getAllDoctorPatients(): AllPatientsResult {
        return try {
            val allPatientsResponse = client.get(Endpoints.DOCTOR_PATIENT_ALL)
            Logger.v("KOD PACJENTOW : ${allPatientsResponse}", tag = "Finito")
            val allPatients: List<PatientResponseDto> = allPatientsResponse.body()
            AllPatientsResult.Success(allPatients)
        } catch (e: Exception) {
            AllPatientsResult.Error(e.message.toString())
        }
    }

    suspend fun getAllMedicalCasesAsDoctor(): AllMedicalCasesResult {
        return try {
            val allPatientsResponse = client.get(Endpoints.DOCTOR_MEDICAL_CASE_ALL)
            val allMedicalCases: List<MedicalCaseResponseDto> = allPatientsResponse.body()
            AllMedicalCasesResult.Success(allMedicalCases)
        } catch (e: Exception) {
            AllMedicalCasesResult.Error(e.message.toString())
        }
    }

    suspend fun changeTreatmentStatus(chosenStatus : MedicalStatus,treatmentId : Int) : Boolean {
        return try {
            val treatmentStatusResponse= client.patch(Endpoints.DOCTOR_TREATMENT_ID_STATUS.withArgs(treatmentId.toString())){
                setBody(StatusRequestDto(chosenStatus))
            }
            when(treatmentStatusResponse.status){
                HttpStatusCode.OK -> true
                else -> throw IllegalStateException(treatmentStatusResponse.bodyAsText())
            }
        }catch(e : Exception){
            Logger.i("failed to change treatment status with error : ${e.message}" , tag ="Treatment Status")
            false
        }
    }

    suspend fun changeMedicationStatus(chosenStatus: MedicalStatus,medicationId:Int) : Boolean {
        return try {
            val medicationStatusResponse= client.patch(Endpoints.DOCTOR_MEDICATION_ID_STATUS.withArgs(medicationId.toString())){
                setBody(StatusRequestDto(chosenStatus))
            }
            when(medicationStatusResponse.status){
                HttpStatusCode.OK -> true
                else -> throw IllegalStateException(medicationStatusResponse.bodyAsText())
            }
        }catch(e : Exception){
            Logger.i("failed to change medication status with error : ${e.message}" , tag ="Medication Status")
            false
        }
    }
}