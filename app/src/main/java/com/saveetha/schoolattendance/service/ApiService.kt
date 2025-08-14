package com.saveetha.schoolattendance.service

import com.saveetha.schoolattendance.myclasses.MyClasses
import com.saveetha.schoolattendance.myclasses.Student
import com.saveetha.schoolattendance.service.request.LoginRequest
import com.saveetha.schoolattendance.service.response.LoginResponse
import com.saveetha.schoolattendance.service.response.ReportResponse
import com.saveetha.schoolattendance.service.response.Users
import com.saveetha.schoolattendance.myclasses.Teacher
import com.saveetha.schoolattendance.myclasses.TeacherData
import com.saveetha.schoolattendance.myclasses.AddStudentRequest
import com.saveetha.schoolattendance.myclasses.ChildAttendance
import com.saveetha.schoolattendance.myclasses.ParentNameResponse

import retrofit2.Call
import retrofit2.http.PUT
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("classlist/getClasses")
    fun getClasses(): Call<List<MyClasses>>

    @POST("users/login")
    fun login(@Body data: LoginRequest): Call < LoginResponse>

    @GET("users/class/{class_id}")
    fun getStudentsIntoClass(@Path("class_id") classId:String): Call<List<Users>>

    @POST("attendance/mark")
    fun attendancemarking(@Body data: Map<String, String>): Call<Map<String, String>>

    @GET("attendance/report/{classId}")
    fun getClassReport(@Path("classId") classId: String): Call<List<ReportResponse>>

    @POST("users/addTeacher")
    fun addTeacher(@Body teacher: TeacherData): Call<Void>

    @GET("users/getTeachers")
    fun getTeachers(): Call<List<Teacher>>

    @POST("users/addStudent")
    fun addStudent(@Body student: AddStudentRequest): Call<Void>

    @GET("users/class/{classId}")
    fun getStudentsByClass(@Path("classId") classId: Int): Call<List<Student>>

    @DELETE("users/teachers/{id}")
    fun deleteTeacher(@Path("id") id: Int): Call<Void>

    @DELETE("users/students/{id}")
    fun deleteStudent(@Path("id") id: Int): Call<Void>

    @GET("users/teachers/{id}")
    fun getTeacherById(@Path("id") id: Int): Call<Teacher>

    @PUT("users/teachers/{id}")
    fun updateTeacher(@Path("id") id: Int, @Body updatedData: Map<String, String>): Call<Void>

    @PUT("users/updateStudent/{id}")
    fun updateStudent(@Path("id") id: Int, @Body student: Student): Call<Void>

    @GET("parent/{parentName}/attendance")
    fun getParentAttendance(@Path("parentName") parentName: String): Call<List<ChildAttendance>>

    @GET("parent/name/{email}")
    fun getParentNameByEmail(@Path("email") email: String): Call<ParentNameResponse>
}

