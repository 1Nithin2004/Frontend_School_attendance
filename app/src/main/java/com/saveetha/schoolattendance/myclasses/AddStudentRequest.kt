package com.saveetha.schoolattendance.myclasses

data class AddStudentRequest(
    val name: String,
    val email: String,
    val phoneNumber: Long,
    val password: String,
    val parentsName: String,
    val studentClass: String,
    val dob: String,
    val userType: String = "Student"
)
