package com.sureprep.dating

// add stuff specific to CPAs

data class User(
    val uid: String? = "",
    val name: String? = "",
    val age: String? = "",
    val email: String? = "",
    val gender: String? = "",
    val preferredGender: String? = "",
    val imageUrl: String? = "",
    val professionalStatus: String? = "",
    val description: String? = ""
)

