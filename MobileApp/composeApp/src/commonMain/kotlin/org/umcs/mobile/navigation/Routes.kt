package org.umcs.mobile.navigation

import kotlinx.serialization.Serializable

object Routes{
    const val NEW_CASE = "new_case"
    const val HOME = "home"
    const val CASE_LIST = "second"
    const val THIRD = "third"
    const val LOGIN = "login"
}

@Serializable
data class Case(
    val uuid : String,
    val stringDate : String
)
