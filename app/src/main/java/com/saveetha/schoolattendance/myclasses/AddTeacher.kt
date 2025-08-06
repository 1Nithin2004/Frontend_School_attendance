package com.saveetha.schoolattendance.myclasses

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.saveetha.schoolattendance.R
import com.saveetha.schoolattendance.service.RetroFit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.saveetha.schoolattendance.model.TeacherData

class AddTeacherActivity : AppCompatActivity() {

    private lateinit var fullName: EditText
    private lateinit var email: EditText
    private lateinit var phone: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var addTeacherButton: Button
    private lateinit var backButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_teacher)

        fullName = findViewById(R.id.fullName)
        email = findViewById(R.id.email)
        phone = findViewById(R.id.phone)
        password = findViewById(R.id.password)
        confirmPassword = findViewById(R.id.confirmPassword)
        addTeacherButton = findViewById(R.id.addTeacherButton)
        backButton = findViewById(R.id.backButton)

        val backArrow = findViewById<ImageView>(R.id.backButton)
        backArrow.setOnClickListener {
            val intent = Intent(this, AdminHomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        addTeacherButton.setOnClickListener {
            val name = fullName.text.toString().trim()
            val mail = email.text.toString().trim()
            val phoneNum = phone.text.toString().trim()
            val pass = password.text.toString().trim()
            val confirmPass = confirmPassword.text.toString().trim()

            if (name.isEmpty() || mail.isEmpty() || phoneNum.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pass != confirmPass) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val teacher = TeacherData(name, mail, phoneNum, pass)
            addTeacherToDB(teacher)
        }
    }

    private fun addTeacherToDB(teacher: TeacherData) {
        val api = RetroFit.getService()

        api.addTeacher(teacher).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AddTeacherActivity, "Teacher added successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@AddTeacherActivity, "Failed to add teacher", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@AddTeacherActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
