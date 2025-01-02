package org.umcs.mobile.testhelpers

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

fun generateRandomMedicationJson(): String {
    // Build the JSON object
    val jsonObject: JsonObject = buildJsonObject {
        put("name", randomString(10))
        put("startDate", getEndDateTime())
        put("endDate", getEndDateTime())
        put("details", randomString(20))
        put("medicalCaseId", 1) // Fixed value
        put("medicalDoctorId", 1) // Fixed value
        put("dosageForm", randomString(15))
        put("strength", randomString(15))
        put("unit", randomString(15))
    }

    // Convert the JSON object to a string
    return Json.encodeToString(JsonObject.serializer(), jsonObject)
}