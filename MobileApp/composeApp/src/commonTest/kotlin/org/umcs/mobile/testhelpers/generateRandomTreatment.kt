package org.umcs.mobile.testhelpers

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

// Helper function to generate a random ISO date-time string using kotlinx-datetime
fun randomIsoDateTime(): String {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    return now.toString()
}

fun generateRandomTreatmentJson(): String {
    // Build the JSON object
    val jsonObject: JsonObject = buildJsonObject {
        put("description", randomString(15))
        put("startDate", randomIsoDateTime())
        put("endDate", randomIsoDateTime())
        put("name", randomString(10))
        put("details", randomString(20))
        put("medicalCaseId", 1) // Fixed value
        put("medicalDoctorId", 1) // Fixed value
    }

    // Convert the JSON object to a string
    return Json.encodeToString(JsonObject.serializer(), jsonObject)
}

