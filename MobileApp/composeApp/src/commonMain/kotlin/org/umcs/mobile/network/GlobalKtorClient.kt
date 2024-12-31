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
import org.umcs.mobile.data.Patient
import org.umcs.mobile.network.Endpoints.withArgs
import org.umcs.mobile.network.dto.case.MedicalCaseRequestDto
import org.umcs.mobile.network.dto.case.MedicalCaseResponseDto
import org.umcs.mobile.network.dto.case.MedicalStatus
import org.umcs.mobile.network.dto.case.MedicationRequestDto
import org.umcs.mobile.network.dto.case.TreatmentRequestDto
import org.umcs.mobile.network.dto.dozapytania.StatusRequestDto
import org.umcs.mobile.network.dto.login.JwtResponseDto
import org.umcs.mobile.network.dto.login.TokenRequestDto
import org.umcs.mobile.network.dto.patient.PatientRequestDto
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

    suspend fun createNewCase(newCase: MedicalCase, doctorId: String) : CreateNewCaseResult {
        return try {
            val medicalCaseRequestDto = MedicalCaseRequestDto(
                patientId = newCase.patient.id,
                admissionDate = LocalDateTime.parse(newCase.admissionDate),
                admissionReason = newCase.admissionReason,
                description = newCase.description,
                attendingDoctorId = doctorId
            )
            val medicalCaseResponse = client.post(Endpoints.MEDICAL_CASE_NEW) {
                setBody(medicalCaseRequestDto)
            }
            when(medicalCaseResponse.status){
                HttpStatusCode.Created ->CreateNewCaseResult.Success
                else -> throw IllegalStateException(medicalCaseResponse.bodyAsText())
            }
        } catch (e: Exception) {
            CreateNewCaseResult.Error(e.message.toString())
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
    ) : CreateNewTreatmentResult{
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
            when (treatmentResponse.status) {
                HttpStatusCode.Created -> CreateNewTreatmentResult.Success
                else -> throw IllegalStateException(treatmentResponse.bodyAsText())
            }
        } catch (e: Exception) {
            CreateNewTreatmentResult.Error(e.message.toString())
        }
    }

    suspend fun createNewMedication(
        newMedication: Medication,
        doctorID: String,
        medicalCaseID: Int,
    ): CreateNewMedicationResult {
        return try {
            val medicationRequestDto = MedicationRequestDto(
                startDate = LocalDate.parse(newMedication.startDate),
                endDate = LocalDate.parse(newMedication.endDate),
                details = newMedication.details,
                name = newMedication.name,
                medicalDoctorId = doctorID,
                medicalCaseId = medicalCaseID,
                dosage = newMedication.dosageForm,
                strength = newMedication.strength,
                unit = newMedication.unit,
                frequency = newMedication.frequency
            )
            val medicationResponse = client.post(Endpoints.DOCTOR_NEW_MEDICATION) {
                setBody(medicationRequestDto)
            }
            when (medicationResponse.status) {
                HttpStatusCode.Created -> CreateNewMedicationResult.Success
                else -> throw IllegalStateException(medicationResponse.bodyAsText())
            }
        } catch (e: Exception) {
            CreateNewMedicationResult.Error(e.message.toString())
        }
    }

    suspend fun getAllDoctorPatients(): AllPatientsResult {
        return try {
            val allPatientsResponse = client.get(Endpoints.DOCTOR_PATIENT_ALL)
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

    suspend fun changeTreatmentStatus(chosenStatus: MedicalStatus, treatmentId: Int): Boolean {
        return try {
            val treatmentStatusResponse =
                client.patch(Endpoints.DOCTOR_TREATMENT_ID_STATUS.withArgs(treatmentId.toString())) {
                    setBody(StatusRequestDto(chosenStatus))
                }
            when (treatmentStatusResponse.status) {
                HttpStatusCode.OK -> true
                else -> throw IllegalStateException(treatmentStatusResponse.bodyAsText())
            }
        } catch (e: Exception) {
            Logger.i(
                "failed to change treatment status with error : ${e.message}",
                tag = "Treatment Status"
            )
            false
        }
    }

    suspend fun changeMedicationStatus(chosenStatus: MedicalStatus, medicationId: Int): Boolean {
        return try {
            val medicationStatusResponse =
                client.patch(Endpoints.DOCTOR_MEDICATION_ID_STATUS.withArgs(medicationId.toString())) {
                    setBody(StatusRequestDto(chosenStatus))
                }
            when (medicationStatusResponse.status) {
                HttpStatusCode.OK -> true
                else -> throw IllegalStateException(medicationStatusResponse.bodyAsText())
            }
        } catch (e: Exception) {
            Logger.i(
                "failed to change medication status with error : ${e.message}",
                tag = "Medication Status"
            )
            false
        }
    }

    suspend fun getAllUnassignedPatients(): AllUnassignedPatientsResult {
        return try {
            val allUnassignedPatientsResponse = client.get(Endpoints.DOCTOR_PATIENT_ALL_UNASSIGNED)
            val allUnassignedPatients: List<PatientResponseDto> =
                allUnassignedPatientsResponse.body()
            AllUnassignedPatientsResult.Success(allUnassignedPatients)
        } catch (e: Exception) {
            AllUnassignedPatientsResult.Error(e.message.toString())
        }
    }

    suspend fun createNewPatient(newPatient: Patient): CreatePatientResult {
        return try {
            val patientRequest = PatientRequestDto(
                socialSecurityNumber = newPatient.socialSecurityNumber,
                firstName = newPatient.firstName,
                lastName = newPatient.lastName,
                age = newPatient.age,
                gender = newPatient.gender,
                address = newPatient.address,
                phoneNumber = newPatient.phoneNumber,
                email = newPatient.email,
                birthDate = LocalDate.parse(newPatient.birthDate)
            )
            val createPatientResponse = client.post(Endpoints.PATIENT_NEW) {
                setBody(patientRequest)
            }
            when (createPatientResponse.status) {
                HttpStatusCode.OK -> CreatePatientResult.Success
                else -> throw IllegalArgumentException(createPatientResponse.bodyAsText())
            }
        } catch (e: Exception) {
            CreatePatientResult.Error(e.message.toString())
        }
    }

    suspend fun importPatient(importedPatientAccessID: String): ImportPatientResult {
        return try {
            val importPatientResponse =
                client.patch(Endpoints.MEDICAL_CASE_ALLOWED_DOCTOR.withArgs(importedPatientAccessID))
            when (importPatientResponse.status) {
                HttpStatusCode.OK -> ImportPatientResult.Success
                else -> throw IllegalStateException(importPatientResponse.bodyAsText())
            }
        } catch (e: Exception) {
            ImportPatientResult.Error(e.message.toString())
        }
    }

    suspend fun closeCase(caseId: Int, force: Boolean): CloseCaseResult {
        return try {
            val closeCaseResponse = client.patch(Endpoints.MEDICAL_CASE_CLOSE.withArgs(caseId.toString())) {
                    url {
                        parameters.append("force", force.toString())
                    }
                }
            when (closeCaseResponse.status) {
                HttpStatusCode.OK -> CloseCaseResult.Success
                else -> throw IllegalStateException(closeCaseResponse.bodyAsText())
            }
        } catch (e: Exception) {
            CloseCaseResult.Error(e.message.toString())
        }
    }
}