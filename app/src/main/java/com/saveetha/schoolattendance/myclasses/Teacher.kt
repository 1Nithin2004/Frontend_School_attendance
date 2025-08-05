package com.saveetha.schoolattendance.myclasses

import com.google.gson.annotations.SerializedName

data class Teacher(
    val name: String,
    val email: String,
    val phone: String,
    val password: String
)

