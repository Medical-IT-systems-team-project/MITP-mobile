package org.umcs.mobile.data

import kotlinx.serialization.Serializable

@Serializable
data class Case(
    val stringDate : String,
    val patientName : String,
    val doctorName : String,
    val caseDetails : String,
    val description : String
){
    fun getFullName() = "$patientName $doctorName"
}