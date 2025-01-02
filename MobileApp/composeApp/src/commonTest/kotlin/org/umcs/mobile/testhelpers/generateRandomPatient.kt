package org.umcs.mobile.testhelpers

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.random.Random

// Helper function to generate a random string of given length
fun randomString(length: Int): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    return (1..length)
        .map { chars[Random.nextInt(chars.length)] }
        .joinToString("")
}

fun randomLetters(length : Int) : String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
    return (1..length)
        .map { chars[Random.nextInt(chars.length)] }
        .joinToString("")
}

// Helper function to generate a random email
fun randomEmail(): String {
    val localPart = randomString(10)
    val domain = randomLetters(5)
    val tld = randomLetters(3)
    return "$localPart@$domain.$tld"
}

fun generateRandomPatientJson(): String {
    // Generate the JSON object with random values
    val patientJson: JsonObject = buildJsonObject {
        put("socialSecurityNumber", Random.nextLong(10000000000L, 99999999999L).toString()) // 11 digits
        put("firstName", randomString(6))
        put("lastName", randomString(6))
        put("age", Random.nextInt(0, 120))
        put("gender", listOf("MALE", "FEMALE").random())
        put("address", randomString(15))
        put("phoneNumber", Random.nextLong(100000000L, 999999999L).toString()) // 9 digits
        put("email", randomEmail())
        put("birthDate", "2024-12-15T14:27:01.075Z") // Fixed birth date
    }

    // Convert the JSON object to a string
    return Json.encodeToString(JsonObject.serializer(), patientJson)
}

fun generateErrorRandomPatientJson(): String {
    // Generate the JSON object with random values
    val patientJson: JsonObject = buildJsonObject {
        put("socialSecurityNumber", Random.nextLong(1000000000L, 9999999999L).toString()) // 10 digits
        put("firstName", "")
        put("lastName", "")
        put("age", -1)
        put("gender", "") // POLE : ERROR
        put("address", "")
        put("phoneNumber", Random.nextLong(10000000L, 99999999L).toString()) // 8 digits
        put("email", "1234@1234.123")
        put("birthDate", "2026-12-15T14:27:01.075Z") // Fixed birth date
    }

    // Convert the JSON object to a string
    return Json.encodeToString(JsonObject.serializer(), patientJson)
}

