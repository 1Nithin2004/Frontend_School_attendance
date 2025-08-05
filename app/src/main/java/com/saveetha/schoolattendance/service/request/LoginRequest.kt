package com.saveetha.schoolattendance.service.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email_address")
    val email: String,

    @SerializedName("password")
    val password: String
)
