package org.umcs.mobile.data

import kotlinx.serialization.Serializable
import org.umcs.mobile.composables.case_view.Medication
import org.umcs.mobile.composables.case_view.Treatment

@Serializable
data class Case(
  //  val caseId : Int,
    val patientName: String,
    val status: CaseStatus,
    val admissionReason: String,
    val admissionDate: String,
    val description: String,
    val createdBy: String,
    val attendingDoctor: String,
    val medications: List<Medication>,
    val treatments: List<Treatment>,
    val allowedDoctors: List<String>,
){
    companion object {
        fun emptyCase() : Case {
            return Case(
        //        caseId = 1,
                patientName = "",
                status = CaseStatus.ONGOING,
                admissionReason = "",
                admissionDate = "",
                description = "",
                createdBy = "",
                attendingDoctor = "",
                medications = emptyList(),
                treatments = emptyList(),
                allowedDoctors = emptyList()
            )
        }
    }
}


enum class CaseStatus {
    ONGOING,
    COMPLETED
}