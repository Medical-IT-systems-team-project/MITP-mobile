package org.umcs.mobile.data

data class Patient(
    var firstName: String = "",
    var lastName: String = "",
    var gender: String = "",
    var socialSecurityNumber: String = "",
    var dateOfBirth: String = "",
    var age: String = "",
)