package com.saveetha.schoolattendance.service.response

data class AttendanceTick(
    val message: String,
    val user: Users
)

data class Users(
    val Full_Name: String,
    val Id: Int,
    val Class: String,
    val Phone_Number: Long,
    val email_address: String,
    val Parents_name: String,
    val Password: String,
    val Date_of_Birth: String,
    val User_type: String
)