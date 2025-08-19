package com.saveetha.schoolattendance.myclasses

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.saveetha.schoolattendance.Objects
import com.saveetha.schoolattendance.R
import com.saveetha.schoolattendance.databinding.ActivityTickAttendanceBinding
import com.saveetha.schoolattendance.service.RetroFit
import com.saveetha.schoolattendance.service.response.Users
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AttendanceTick : AppCompatActivity() {

    private lateinit var binding: ActivityTickAttendanceBinding
    private lateinit var tableLayout: TableLayout
    private var studentsList: List<Users>? = null
    private lateinit var dialog: android.app.AlertDialog
    private lateinit var sf: SharedPreferences
    private var classId: String = ""

    private val attendanceStatus = mutableMapOf<String, String>() // Temporary storage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTickAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tableLayout = binding.tableLayout

        classId = intent.getStringExtra("class") ?: ""
        sf = getSharedPreferences("login", Context.MODE_PRIVATE)
        dialog = Objects.showProgressDialog(this)

        binding.submitAttendanceBtn.setOnClickListener { showSubmitDialog() }

        getStudents(classId)
    }

    private fun showSubmitDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_submit_attendance, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)

        val alertDialog = dialogBuilder.create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog.show()

        val yesBtn = dialogView.findViewById<Button>(R.id.btnYes)
        val noBtn = dialogView.findViewById<Button>(R.id.btnNo)

        yesBtn.setOnClickListener {
            alertDialog.dismiss()

            // Check if all students are marked
            val allMarked = studentsList?.all { student ->
                attendanceStatus.containsKey(student.Id.toString())
            } ?: false

            if (!allMarked) {
                Toast.makeText(this@AttendanceTick, "Please mark attendance for all students", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Submit attendance
            val teacherId = sf.getInt("user_id", 0).toString()
            var totalRequests = attendanceStatus.size
            var completedRequests = 0

            for ((studentId, status) in attendanceStatus) {
                RetroFit.getService().attendancemarking(
                    mapOf(
                        "studentId" to studentId,
                        "teacherId" to teacherId,
                        "date" to Objects.currentDate,
                        "classId" to classId,
                        "status" to status
                    )
                ).enqueue(object : Callback<Map<String, String>> {
                    override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                        completedRequests++
                        if (completedRequests == totalRequests) {
                            Toast.makeText(this@AttendanceTick, "Attendance marked successfully", Toast.LENGTH_SHORT).show()
                            navigateBack()
                        }
                    }

                    override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                        completedRequests++
                        if (completedRequests == totalRequests) {
                            Toast.makeText(this@AttendanceTick, "Attendance marked successfully", Toast.LENGTH_SHORT).show()
                            navigateBack()
                        }
                    }
                })
            }
        }

        noBtn.setOnClickListener { alertDialog.dismiss() }
    }

    private fun navigateBack() {
        val intent = Intent(this@AttendanceTick, MarkorReportActivity::class.java)
        intent.putExtra("class_name", classId)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        finish()
    }

    private fun getStudents(classId: String) {
        dialog.show()
        RetroFit.getService().getStudentsIntoClass(classId).enqueue(object : Callback<List<Users>> {
            override fun onResponse(call: Call<List<Users>>, response: Response<List<Users>>) {
                dialog.dismiss()
                if (response.isSuccessful) {
                    val res = response.body()!!
                    if (res.isNotEmpty()) {
                        studentsList = res
                        setTableData()
                    }
                }
            }

            override fun onFailure(call: Call<List<Users>>, t: Throwable) {
                dialog.dismiss()
            }
        })
    }

    private fun setTableData() {
        val studentList = studentsList?.map { it.Full_Name } ?: emptyList()

        for ((index, name) in studentList.withIndex()) {
            val tableRow = TableRow(this)
            tableRow.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )

            val serial = TextView(this)
            serial.text = (index + 1).toString()
            serial.setPadding(8, 8, 8, 8)

            val studentName = TextView(this)
            studentName.text = name
            studentName.setPadding(8, 8, 8, 8)

            val presentCheckBox = CheckBox(this)
            val absentCheckBox = CheckBox(this)

            // Store selection locally
            presentCheckBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    absentCheckBox.isChecked = false
                    attendanceStatus[studentsList!![index].Id.toString()] = "present"
                } else {
                    attendanceStatus.remove(studentsList!![index].Id.toString())
                }
            }

            absentCheckBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    presentCheckBox.isChecked = false
                    attendanceStatus[studentsList!![index].Id.toString()] = "absent"
                } else {
                    attendanceStatus.remove(studentsList!![index].Id.toString())
                }
            }

            tableRow.addView(serial)
            tableRow.addView(studentName)
            tableRow.addView(presentCheckBox)
            tableRow.addView(absentCheckBox)

            tableLayout.addView(
                tableRow,
                TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            )
        }
    }
}
