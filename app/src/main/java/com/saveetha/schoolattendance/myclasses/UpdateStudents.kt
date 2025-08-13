package com.saveetha.schoolattendance.myclasses

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.saveetha.schoolattendance.R
import com.saveetha.schoolattendance.service.RetroFit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateStudentActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var phoneInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var classInput: EditText
    private lateinit var parentNameInput: EditText
    private lateinit var saveButton: Button
    private lateinit var backArrow: ImageView

    private var studentId: Int = 0
    private var dob: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_student)

        // Find views
        nameInput = findViewById(R.id.editstudentName)
        emailInput = findViewById(R.id.editstudentEmail)
        phoneInput = findViewById(R.id.editstudentPhone)
        passwordInput = findViewById(R.id.editstudnetPassword)
        classInput = findViewById(R.id.etClass)
        parentNameInput = findViewById(R.id.editstudentParent)
        saveButton = findViewById(R.id.btnSave)
        backArrow = findViewById(R.id.backArrow)

        // Get data from intent
        studentId = intent.getIntExtra("studentId", 0)
        nameInput.setText(intent.getStringExtra("studentName"))
        emailInput.setText(intent.getStringExtra("studentEmail"))
        phoneInput.setText(intent.getStringExtra("studentPhone"))
        passwordInput.setText(intent.getStringExtra("studentPassword"))
        classInput.setText(intent.getStringExtra("studentClass"))
        parentNameInput.setText(intent.getStringExtra("parentName"))
        dob = intent.getStringExtra("studentDob") ?: ""

        // Back button
        backArrow.setOnClickListener { finish() }

        // Save button click
        saveButton.setOnClickListener { updateStudent() }
    }

    private fun updateStudent() {
        val updatedStudent = Student(
            fullName = nameInput.text.toString(),
            id = studentId,
            studentClass = classInput.text.toString(),
            phoneNumber = phoneInput.text.toString().toLongOrNull() ?: 0L,
            email = emailInput.text.toString(),
            parentsName = parentNameInput.text.toString(),
            password = passwordInput.text.toString(),
            dob = dob,
            userType = "Student"
        )

        RetroFit.getService().updateStudent(studentId, updatedStudent)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@UpdateStudentActivity,
                            "Student updated successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@UpdateStudentActivity,
                            "Update failed: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(
                        this@UpdateStudentActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
