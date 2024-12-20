package org.umcs.mobile.testhelpers

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

// Helper function to generate a random ISO date-time string using kotlinx-datetime
fun getEndDateTime(): String {
    val endDate = LocalDateTime.parse("2025-09-15T10:00:00")
    return endDate.toString()
}

fun getStartDateTime(): String {
    val startDate = LocalDateTime.parse("2025-01-15T10:00:00")
    return startDate.toString()
}

fun generateRandomTreatmentJson(): String {
    // Build the JSON object
    val jsonObject: JsonObject = buildJsonObject {
        put("description", randomString(15))
        put("startDate", getStartDateTime())
        put("endDate", getEndDateTime())
        put("name", randomString(10))
        put("details", randomString(20))
        put("medicalCaseId", 1) // Fixed value
        put("medicalDoctorId", 1) // Fixed value
    }

    // Convert the JSON object to a string
    return Json.encodeToString(JsonObject.serializer(), jsonObject)
}

