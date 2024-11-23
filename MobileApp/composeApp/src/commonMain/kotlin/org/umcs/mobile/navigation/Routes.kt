package org.umcs.mobile.navigation

import androidx.core.bundle.Bundle
import androidx.navigation.NavType
import com.eygraber.uri.UriCodec
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.umcs.mobile.data.Case
import org.umcs.mobile.data.Patient

sealed class Routes {
    @Serializable
    data object Home : Routes()

    @Serializable
    data object NewPatient : Routes()

    @Serializable
    data object ChooseLogin : Routes()

    @Serializable
    data object CaseListPatient : Routes()

    @Serializable
    data object CaseListDoctor : Routes()

    @Serializable
    data object DoctorLogin : Routes()

    @Serializable
    data object PatientLogin : Routes()

    @Serializable
    data object NewCase : Routes()

    @Serializable
    data class CaseDetails(val case: Case) : Routes()

    @Serializable
    data class ImportPatient(val patient: Patient) : Routes()

    @Serializable
    data class ShareUUID(val patient: Patient) : Routes()
}

object CustomNavType {
    val CaseType = object : NavType<Case>(
        isNullableAllowed = false
    ) {
        override fun get(bundle: Bundle, key: String): Case? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): Case {
            return Json.decodeFromString(UriCodec.decode(value))
        }

        override fun serializeAsValue(value: Case): String {
            return UriCodec.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: Case) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }
    val PatientType = object : NavType<Patient>(
        isNullableAllowed = false
    ) {
        override fun get(bundle: Bundle, key: String): Patient? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): Patient {
            return Json.decodeFromString(UriCodec.decode(value))
        }

        override fun serializeAsValue(value: Patient): String {
            return UriCodec.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: Patient) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }
}
