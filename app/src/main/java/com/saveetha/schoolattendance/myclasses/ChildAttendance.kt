package com.saveetha.schoolattendance.myclasses

data class ChildAttendance(
    val Full_Name: String,
    val Class: String,
    val classes_present: String,
    val classes_absent: String,
    val total: Int,
    val percentage: String
)
