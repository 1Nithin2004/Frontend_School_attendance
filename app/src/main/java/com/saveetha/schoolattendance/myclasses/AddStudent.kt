package com.saveetha.schoolattendance.myclasses

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.saveetha.schoolattendance.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import android.view.MenuItem
import android.widget.ImageView

class AddStudentActivity : AppCompatActivity() {

    // Declare all your views using lateinit var
    private lateinit var classSpinner: Spinner
    private lateinit var studentNameEditText: EditText
    private lateinit var dateOfBirthEditText: EditText
    private lateinit var addStudentButton: Button  // Use Button instead of LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the content view to your layout XML file
        supportActionBar?.hide()

        setContentView(R.layout.activity_add_student)

        // --- Initialize Views by finding them by their IDs ---
        // Ensure these IDs match the IDs in your activity_add_student.xml
        classSpinner = findViewById(R.id.classSpinner)
        studentNameEditText = findViewById(R.id.studentName) // Assuming ID is studentNameEditText
        dateOfBirthEditText = findViewById(R.id.dateOfBirth)
        addStudentButton = findViewById(R.id.saveButton)  // Correct ID from layout

        val backArrow = findViewById<ImageView>(R.id.back_arrow)
        backArrow.setOnClickListener {
            // Go back to AdminHomeActivity
            val intent = Intent(this, AdminHomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish() // optional, closes current activity
        }

        // --- Spinner Setup ---
        // This sets up the dropdown options for your class spinner
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.class_options, // Make sure you have this array defined in res/values/strings.xml
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        classSpinner.adapter = adapter

        // --- Date Picker Setup ---
        // When the dateOfBirthEditText is clicked, show the DatePickerDialog
        dateOfBirthEditText.setOnClickListener {
            showDatePickerDialog()
        }

        // --- Add Student Button Listener ---
        // Set an OnClickListener for your "Add Student" button
        addStudentButton.setOnClickListener {
            // Retrieve data from input fields
            val studentName = studentNameEditText.text.toString().trim()
            val dob = dateOfBirthEditText.text.toString().trim()
            val selectedClass = classSpinner.selectedItem.toString()

            // Basic validation: Check if fields are empty
            // Assuming "Select Class" is a placeholder/hint for the spinner
            if (studentName.isEmpty() || dob.isEmpty() || selectedClass.isEmpty() || selectedClass == "Select Class") {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            } else {
                // If all data is present, you would typically proceed to:
                // 1. Create a data object (e.g., a Student object)
                // 2. Make an API call to your backend to save the student data
                // 3. Show a success message or navigate back to the previous screen

                // For now, let's just show a Toast message with the collected data
                Toast.makeText(this, "Adding Student: $studentName, DOB: $dob, Class: $selectedClass", Toast.LENGTH_LONG).show()

                // Example of what you might do next:
                // finish() // To close this activity and go back to the previous one
                // val resultIntent = Intent()
                // resultIntent.putExtra("newStudentAdded", true)
                // setResult(Activity.RESULT_OK, resultIntent)
                // finish()
            }
        }
    }

    /**
     * Shows a DatePickerDialog to allow the user to select a date.
     * The selected date is then formatted and set to the dateOfBirthEditText.
     */
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                val selectedDateCalendar = Calendar.getInstance()
                selectedDateCalendar.set(selectedYear, selectedMonth, selectedDayOfMonth)

                // Format the date as "yyyy / MM / dd" (e.g., "2025 / 08 / 01")
                val dateFormat = SimpleDateFormat("yyyy / MM / dd", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDateCalendar.time)

                dateOfBirthEditText.setText(formattedDate)
            },
            year,
            month,
            day
        )

        // Optional: Set a maximum date to prevent selecting future dates
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

        datePickerDialog.show()
    }
}