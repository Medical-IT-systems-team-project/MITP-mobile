package org.umcs.mobile.network.dto.case

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.umcs.mobile.composables.case_view.Medication
import org.umcs.mobile.composables.case_view.Treatment
import org.umcs.mobile.data.Case
import org.umcs.mobile.data.CaseStatus
import org.umcs.mobile.network.dto.serializer.LocalDateTimeSerializer

@Serializable
data class MedicalCaseResponseDto  (
 //   val caseId: Int,
    val patientName : String,
    val status: CaseStatus,
    val admissionReason : String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val admissionDate : LocalDateTime,
    val description : String,
    val createdBy : String,
    val attendingDoctor : String,
    val medications : List<MedicationResponseDto>,
    val treatments : List<TreatmentResponseDto>,
    val allowedDoctors : List<String>
)

fun List<MedicalCaseResponseDto>.toCaseList() : List<Case>{
    return this.map { it.toCase() }
}

fun MedicalCaseResponseDto.toCase(): Case{
    val mappedMedication = medications.map {it.toMedication()}.toMutableList()
    val mappedTreatment = treatments.map{it.toTreatment()}.toMutableList()
    
    return Case(
        patientName = patientName,
        status = status,
        admissionReason = admissionReason,
        admissionDate = admissionDate.toString().replace(oldChar = 'T' , newChar = ' '),
        description = description,
        createdBy = createdBy,
        attendingDoctor = attendingDoctor,
        medications = mappedMedication,
        treatments = mappedTreatment,
        allowedDoctors = allowedDoctors,
   //     caseId = caseId
    )
}

fun TreatmentResponseDto.toTreatment() : Treatment{
    return Treatment(
        name = name,
        startDate = startDate.toString(),
        endDate = endDate.toString(),
        details = details,
        createdBy = medicalDoctorName,
        status = status,
        //treatmentId = treatmentId
    )
}

fun MedicationResponseDto.toMedication(): Medication {
    return Medication(
        name = name,
        startDate = startDate.toString(),
        endDate = endDate.toString(),
        dosage = dosage,
        frequency = frequency,
        prescribedBy = medicalDoctorName,
        strength = strength,
        unit = unit,
        status = status,
      //  medicationId = medicationId
    )
}