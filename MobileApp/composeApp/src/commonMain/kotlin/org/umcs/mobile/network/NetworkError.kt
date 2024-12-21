package org.umcs.mobile.network

import org.umcs.mobile.network.dto.patient.PatientResponseDto

sealed class LoginResult {
    data class Success(val patient: PatientResponseDto) : LoginResult()
    data class Error(val message: String) : LoginResult()
}