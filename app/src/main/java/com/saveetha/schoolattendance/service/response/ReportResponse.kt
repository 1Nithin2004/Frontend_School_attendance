// ReportResponse.kt
package com.saveetha.schoolattendance.service.response  // or your actual package

data class ReportResponse(
    val student_id: Int,
    val name: String,
    val attendance_percentage: Float
)
