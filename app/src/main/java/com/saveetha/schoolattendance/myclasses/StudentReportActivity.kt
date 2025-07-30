package com.saveetha.schoolattendance.myclasses

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import android.widget.TableRow
import android.widget.TextView
import android.widget.TableLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.saveetha.schoolattendance.R
import com.saveetha.schoolattendance.service.RetroFit
import com.saveetha.schoolattendance.service.response.ReportResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudentReportActivity : AppCompatActivity() {

    private lateinit var tableLayout: TableLayout
    private var classId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_report)

        tableLayout = findViewById(R.id.tableLayoutReport)
        classId = intent.getStringExtra("classId") ?: ""

        findViewById<ImageView>(R.id.backButton).setOnClickListener {
            finish()
        }

        // Fetch report for selected class
        getReportByClass(classId)
    }

    private fun getReportByClass(classId: String) {
        // Call API like: /attendance/report/:classId
        RetroFit.getService().getClassReport(classId).enqueue(object : Callback<List<ReportResponse>> {
            override fun onResponse(
                call: Call<List<ReportResponse>>,
                response: Response<List<ReportResponse>>
            ) {
                if (response.isSuccessful) {
                    loadReport(response.body()!!)
                } else {
                    Toast.makeText(this@StudentReportActivity, "Error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ReportResponse>>, t: Throwable) {
                Toast.makeText(this@StudentReportActivity, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadReport(reportList: List<ReportResponse>) {
        for ((index, student) in reportList.withIndex()) {
            val row = TableRow(this)

            val sn = TextView(this).apply {
                text = (index + 1).toString()
                setPadding(8, 8, 8, 8)
            }

            val name = TextView(this).apply {
                text = student.name
                setPadding(8, 8, 8, 8)
            }

            val percent = TextView(this).apply {
                text = "${student.attendance_percentage}%"
                setTextColor(Color.WHITE)
                setPadding(16, 8, 16, 8)
                setBackgroundColor(
                    Color.parseColor(
                        if (student.attendance_percentage < 60) "#F44336" else "#4CAF50"
                    )
                )
            }

            row.addView(sn)
            row.addView(name)
            row.addView(percent)

            tableLayout.addView(row)
        }
    }
}
