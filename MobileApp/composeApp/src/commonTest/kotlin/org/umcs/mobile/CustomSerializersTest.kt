package org.umcs.mobile

import kotlinx.serialization.json.Json
import org.umcs.mobile.network.dto.patient.PatientRequestDto
import kotlin.test.Test
import kotlin.test.assertFailsWith

class CustomSerializersTest {

    private val json = Json { isLenient = true; ignoreUnknownKeys = true }



    @Test
    fun testSSNPatternSerializer() {
        val validSSN = """{
            "socialSecurityNumber": "12345678901",
            "firstName": "John",
            "lastname": "Doe",
            "age": 30,
            "gender": "Male",
            "address": "123 Main St",
            "phoneNumber": "123456789",
            "email": "test@example.com",
            "birthDate": "1990-01-01"
        }"""
        val invalidSSN = """{
            "socialSecurityNumber": "12345",
            "firstName": "John",
            "lastname": "Doe",
            "age": 30,
            "gender": "Male",
            "address": "123 Main St",
            "phoneNumber": "123456789",
            "email": "test@example.com",
            "birthDate": "1990-01-01"
        }"""

        // Test valid SSN
        json.decodeFromString(PatientRequestDto.serializer(), validSSN)

        // Test invalid SSN
        assertFailsWith<IllegalArgumentException> {
            json.decodeFromString(PatientRequestDto.serializer(), invalidSSN)
        }
    }

    @Test
    fun testPhoneNumberPatternSerializer() {
        val validPhoneNumber = """{
            "socialSecurityNumber": "12345678901",
            "firstName": "John",
            "lastname": "Doe",
            "age": 30,
            "gender": "Male",
            "address": "123 Main St",
            "phoneNumber": "123456789",
            "email": "test@example.com",
            "birthDate": "1990-01-01"
        }"""
        val invalidPhoneNumber = """{
            "socialSecurityNumber": "12345678901",
            "firstName": "John",
            "lastname": "Doe",
            "age": 30,
            "gender": "Male",
            "address": "123 Main St",
            "phoneNumber": "12345",
            "email": "test@example.com",
            "birthDate": "1990-01-01"
        }"""

        // Test valid phone number
        json.decodeFromString(PatientRequestDto.serializer(), validPhoneNumber)

        // Test invalid phone number
        assertFailsWith<IllegalArgumentException> {
            json.decodeFromString(PatientRequestDto.serializer(), invalidPhoneNumber)
        }
    }

    @Test
    fun testEmailPatternSerializer() {
        val validEmail = """{
            "socialSecurityNumber": "12345678901",
            "firstName": "John",
            "lastname": "Doe",
            "age": 30,
            "gender": "Male",
            "address": "123 Main St",
            "phoneNumber": "123456789",
            "email": "test@example.com",
            "birthDate": "1990-01-01"
        }"""
        val invalidEmail = """{
            "socialSecurityNumber": "12345678901",
            "firstName": "John",1
            "lastname": "Doe",
            "age": 30,
            "gender": "Male",
            "address": "123 Main St",
            "phoneNumber": "123456789",
            "email": "invalid-email",
            "birthDate": "1990-01-01"
        }"""

        // Test valid email
        json.decodeFromString(PatientRequestDto.serializer(), validEmail)

        // Test invalid email
        assertFailsWith<IllegalArgumentException> {
            json.decodeFromString(PatientRequestDto.serializer(), invalidEmail)
        }
    }

    @Test
    fun testBirthDatePatternSerializer() {
        val validBirthDate = """{
            "socialSecurityNumber": "12345678901",
            "firstName": "John",
            "lastname": "Doe",
            "age": 30,
            "gender": "Male",
            "address": "123 Main St",
            "phoneNumber": "123456789",
            "email": "test@example.com",
            "birthDate": "1990-01-01"
        }"""
        val invalidBirthDate = """{
            "socialSecurityNumber": "12345678901",
            "firstName": "John",
            "lastname": "Doe",
            "age": 30,
            "gender": "Male",
            "address": "123 Main St",
            "phoneNumber": "123456789",
            "email": "test@example.com",
            "birthDate": "01-01-1990"
        }"""

        // Test valid birth date
        json.decodeFromString(PatientRequestDto.serializer(), validBirthDate)

        // Test invalid birth date
        assertFailsWith<IllegalArgumentException> {
            json.decodeFromString(PatientRequestDto.serializer(), invalidBirthDate)
        }
    }
}