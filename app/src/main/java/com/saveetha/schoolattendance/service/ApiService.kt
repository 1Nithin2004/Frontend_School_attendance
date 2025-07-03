package com.saveetha.schoolattendance.service

import com.saveetha.schoolattendance.myclasses.MyClasses
import com.saveetha.schoolattendance.service.request.LoginRequest
import com.saveetha.schoolattendance.service.response.LoginResponse
import com.saveetha.schoolattendance.service.response.Users
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.TreeSet

interface ApiService {

    @GET("classlist/getClasses")
    fun getClasses(): Call<List<MyClasses>>

    @POST("users/login")
    fun login(@Body data: LoginRequest): Call < LoginResponse>

    @GET("users/class/{class_id}")
    fun getStudentsIntoClass(@Path("class_id") classId:String): Call<List<Users>>
}