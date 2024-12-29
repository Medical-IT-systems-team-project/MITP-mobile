package org.umcs.mobile.network

import org.umcs.mobile.network.dto.case.MedicalCaseResponseDto
import org.umcs.mobile.network.dto.login.JwtResponseDto
import org.umcs.mobile.network.dto.patient.PatientResponseDto

sealed class PatientLoginResult {
    data class Success(val patient: PatientResponseDto) : PatientLoginResult()
    data class Error(val message: String) : PatientLoginResult()
}

sealed class DoctorLoginResult {
    data class Success(val patient: JwtResponseDto) : DoctorLoginResult()
    data class Error(val message: String) : DoctorLoginResult()
}

sealed class AllPatientsResult {
    data class Success(val patients: List<PatientResponseDto>) : AllPatientsResult()
    data class Error(val message: String) : AllPatientsResult()
}

sealed class AllMedicalCasesResult {
    data class Success(val cases: List<MedicalCaseResponseDto>) : AllMedicalCasesResult()
    data class Error(val message: String) : AllMedicalCasesResult()
}

sealed class CreatePatientResult {
    data object Success : CreatePatientResult()
    data class Error(val message: String) : CreatePatientResult()
}