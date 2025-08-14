package com.saveetha.schoolattendance.myclasses

data class ChildAttendance(
    val student_id: Int,
    val name: String,
    val total_days: Int,
    val present_days: Int,
    val absent_days: Int,
    val attendance_percentage: String,
    val status: String // green, yellow, red
)
