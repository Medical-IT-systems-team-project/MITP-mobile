package org.umcs.mobile.data

import kotlinx.serialization.Serializable

@Serializable
data class Patient(
    val socialSecurityNumber: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val age: String = "",
    val gender: String = "",
    val address: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val birthDate: String = "",
    val accessID: String = "",
) {
    fun getFullName(): String {
        return "$firstName $lastName"
    }

    companion object {
        fun emptyPatient(): Patient {
            return Patient(
                socialSecurityNumber = "",
                firstName = "",
                lastName = "",
                age = "",
                gender = "",
                address = "",
                phoneNumber = "",
                email = "",
                birthDate = "",
                accessID = ""
            )
        }
    }
}