package com.saveetha.schoolattendance.myclasses

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.saveetha.schoolattendance.R
import com.saveetha.schoolattendance.myclasses.AddStudentRequest
import com.saveetha.schoolattendance.service.RetroFit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class AddStudentActivity : AppCompatActivity() {

    private lateinit var studentNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var parentNameEditText: EditText
    private lateinit var dobEditText: EditText
    private lateinit var classSpinner: Spinner
    private lateinit var addStudentButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        // Initialize views
        studentNameEditText = findViewById(R.id.studentName)
        emailEditText = findViewById(R.id.email)
        phoneEditText = findViewById(R.id.phoneNumber)
        passwordEditText = findViewById(R.id.password)
        parentNameEditText = findViewById(R.id.parentName)
        dobEditText = findViewById(R.id.dateOfBirth)
        classSpinner = findViewById(R.id.classSpinner)
        addStudentButton = findViewById(R.id.saveButton)

        // Set up class dropdown
        val classList = listOf("6", "7", "8", "9", "10")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, classList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        classSpinner.adapter = adapter

        // Date picker
        dobEditText.setOnClickListener {
            showDatePicker()
        }

        // Add student
        addStudentButton.setOnClickListener {
            addStudent()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = "$year/${month + 1}/$dayOfMonth"
                dobEditText.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun addStudent() {
        val name = studentNameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val phoneStr = phoneEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val parentName = parentNameEditText.text.toString().trim()
        val dob = dobEditText.text.toString().trim()
        val studentClass = classSpinner.selectedItem.toString()

        if (name.isEmpty() || email.isEmpty() || phoneStr.isEmpty() || password.isEmpty() ||
            parentName.isEmpty() || dob.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val phone = try {
            phoneStr.toLong()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show()
            return
        }

        val request = AddStudentRequest(
            name = name,
            email = email,
            phoneNumber = phone,
            password = password,
            parentsName = parentName,
            studentClass = studentClass,
            dob = dob
        )

        RetroFit.getService().addStudent(request).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AddStudentActivity, "Student added successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@AddStudentActivity, "Failed to add student: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@AddStudentActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
