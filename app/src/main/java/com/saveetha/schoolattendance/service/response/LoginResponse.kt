package com.saveetha.schoolattendance.service.response

    data class LoginResponse(
        val message: String,
        val user: User
    )

    data class User(
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

