package org.umcs.mobile.network

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