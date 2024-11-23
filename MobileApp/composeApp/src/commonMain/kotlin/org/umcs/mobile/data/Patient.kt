package org.umcs.mobile.data

import kotlinx.serialization.Serializable

//TODO : Create some sort of wrapper for navigation purposes
@Serializable
data class Patient(
    var firstName: String = "",
    var lastName: String = "",
    var gender: String = "",
    var socialSecurityNumber: String = "",
    var dateOfBirth: String = "",
    var age: String = "",
){
    fun getFullName(): String {
        return "$firstName $lastName"
    }
}