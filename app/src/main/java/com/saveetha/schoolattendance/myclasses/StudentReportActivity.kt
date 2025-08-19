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
import androidx.recyclerview.widget.RecyclerView
import com.saveetha.schoolattendance.R
import com.saveetha.schoolattendance.service.RetroFit
import com.saveetha.schoolattendance.service.response.ReportResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudentReportActivity : AppCompatActivity() {

    private lateinit var percentageListRV: RecyclerView
    private var classId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_report)
        classId = intent.getStringExtra("classId") ?: ""

        percentageListRV = findViewById(R.id.percentageListRV)

        findViewById<ImageView>(R.id.backButton).setOnClickListener { finish() }

        getReportByClass(classId)
    }

    private fun getReportByClass(classId: String) {
        RetroFit.getService().getClassReport(classId).enqueue(object : Callback<List<ReportResponse>> {
            override fun onResponse(
                call: Call<List<ReportResponse>>,
                response: Response<List<ReportResponse>>
            ) {
                if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                    val adapter = ReportAdapter(response.body()!!)
                    percentageListRV.adapter = adapter
                } else {
                    Toast.makeText(this@StudentReportActivity, "No data found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ReportResponse>>, t: Throwable) {
                Toast.makeText(this@StudentReportActivity, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
