package org.umcs.mobile.network

object Endpoints { //TODO : figure out how to use this without String.format()
    const val UPDATE_LOGIN = "update/%s"
    const val REGISTER = "register"
    const val FIND_LOGIN = "find/%s"
    const val DELETE_LOGIN = "delete/%s"

    // patient-controller
    const val PATIENT_NEW = "patient/new"
    const val PATIENT_ACCESS_ID = "patient/%s"

    // medical-case-controller
    const val MEDICAL_CASE_NEW = "medical-case/new"
    const val MEDICAL_CASE_ID = "medical-case/%s"
    const val MEDICAL_CASE_ACCESS_ID_SUMMARY = "medical-case/%s/summary"
    const val MEDICAL_CASE_ACCESS_ID_HISTORY = "medical-case/%s/history"
    const val MEDICAL_CASE_ID_TREATMENT_ALL = "medical-case/%s/treatment/all"
    const val MEDICAL_CASE_ID_MEDICATION_ALL = "medical-case/%s/medication/all"

    const val LOGIN = "login"
    // doctor-controller
    const val DOCTOR_NEW_TREATMENT = "doctor/treatment/new"
    const val DOCTOR_NEW_MEDICATION = "doctor/medication/new"
    const val DOCTOR_TREATMENT_ID_STATUS = "doctor/treatment/%s/status"
    const val DOCTOR_MEDICATION_ID_STATUS = "doctor/medication/%s/status"
    const val DOCTOR_PATIENT_ALL = "doctor/patient/all"
    const val DOCTOR_MEDICAL_CASE_ALL = "doctor/medical-case/all"

    fun String.withArgs(vararg args: String): String {
        return this.replace("%s", args.joinToString("/"))
    }
}