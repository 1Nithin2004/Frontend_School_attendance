package com.saveetha.schoolattendance.myclasses

import com.google.gson.annotations.SerializedName

data class Teacher(
    @SerializedName("Full_Name") val fullName: String,
    @SerializedName("Id") val id: Int,
    @SerializedName("Class") val teacherClass: String,
    @SerializedName("Phone_Number") val phoneNumber: Long,
    @SerializedName("email_address") val email: String,
    @SerializedName("Parents_name") val parentsName: String,
    @SerializedName("Password") val password: String,
    @SerializedName("Date_of_Birth") val dob: String,
    @SerializedName("User_type") val userType: String
)
