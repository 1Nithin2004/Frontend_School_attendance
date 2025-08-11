package com.saveetha.schoolattendance.myclasses

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.saveetha.schoolattendance.R
import com.saveetha.schoolattendance.service.RetrofitClient
import retrofit2.Call
import android.util.Log
import retrofit2.Callback
import retrofit2.Response

class UpdateTeachersActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var etPassword: EditText
    private lateinit var etClass: EditText
    private lateinit var btnSave: Button
    private lateinit var backArrow: ImageView

    private var teacherId: Int = -1  // will come from Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_teachers)

        // Bind views
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etPhone)
        etPassword = findViewById(R.id.etPassword)
        etClass = findViewById(R.id.etClass)
        btnSave = findViewById(R.id.btnSave)
        backArrow = findViewById(R.id.backArrow)

        teacherId = intent.getIntExtra("teacher_id", -1) // match MyTeachersActivity
        Toast.makeText(this, "Teacher ID: $teacherId", Toast.LENGTH_SHORT).show()
        if (teacherId == -1) {
            Toast.makeText(this, "Invalid Teacher ID", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Load teacher details
        loadTeacherDetails()

        // Save changes
        btnSave.setOnClickListener {
            saveTeacherDetails()
        }

        // Back arrow
        backArrow.setOnClickListener { finish() }
    }

    private fun loadTeacherDetails() {
        // This is the one-line fix: we're passing the Int directly.
        RetrofitClient.instance.getTeacherById(teacherId)
            .enqueue(object : Callback<Teacher> {
                override fun onResponse(call: Call<Teacher>, response: Response<Teacher>) {
                    Log.d("UpdateTeacher", "Response code: ${response.code()}")
                    if (response.isSuccessful && response.body() != null) {
                        val teacher = response.body()!!
                        etName.setText(teacher.fullName)
                        Log.d("UpdateTeacher", "Teacher loaded: ${teacher.fullName}")
                        etEmail.setText(teacher.email)
                        etPhone.setText(teacher.phoneNumber)
                        etPassword.setText(teacher.password)
                        etClass.setText(teacher.teacherClass)
                    } else {
                        Log.e("UpdateTeacher", "Response unsuccessful or empty")
                        Toast.makeText(this@UpdateTeachersActivity, "Failed to load teacher", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Teacher>, t: Throwable) {
                    Log.e("UpdateTeacher", "API call failed: ${t.message}")
                    Toast.makeText(this@UpdateTeachersActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun saveTeacherDetails() {
        val updatedData = mapOf(
            "Full_Name" to etName.text.toString(),
            "Class" to etClass.text.toString(),
            "Phone_Number" to etPhone.text.toString(),
            "email_address" to etEmail.text.toString(),
            "Password" to etPassword.text.toString()
        )
        RetrofitClient.instance.updateTeacher(teacherId, updatedData)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@UpdateTeachersActivity, "Teacher updated successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@UpdateTeachersActivity, "Update failed", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@UpdateTeachersActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
