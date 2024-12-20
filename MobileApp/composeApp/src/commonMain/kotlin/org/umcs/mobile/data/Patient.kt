package org.umcs.mobile.data

import kotlinx.serialization.Serializable

@Serializable
data class Patient(
    var socialSecurityNumber: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var age: String = "",
    var gender: String = "",
    var address: String = "",
    var phoneNumber: String = "",
    var email: String = "",
    var birthDate: String = "",
){
    fun getFullName(): String {
        return "$firstName $lastName"
    }
}