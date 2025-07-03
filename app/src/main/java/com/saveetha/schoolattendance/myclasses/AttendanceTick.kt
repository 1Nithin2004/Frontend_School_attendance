package com.saveetha.schoolattendance.myclasses

import android.os.Bundle
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.saveetha.schoolattendance.databinding.ActivityTickAttendanceBinding
import com.saveetha.schoolattendance.service.RetroFit
import com.saveetha.schoolattendance.service.response.Users
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AttendanceTick : AppCompatActivity() {

    private lateinit var binding: ActivityTickAttendanceBinding

    private lateinit var tableLayout: TableLayout

    private var studentsList:List<Users>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTickAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tableLayout = binding.tableLayout

        val classId = intent.getStringExtra("class").toString()

        getStudents(classId)
    }

    private fun getStudents(classId:String) {

        RetroFit.getService().getStudentsIntoClass(classId).enqueue(object:Callback<List<Users>> {
            override fun onResponse(call: Call<List<Users>>, response: Response<List<Users>>) {
                if(response.isSuccessful) {
                    val res = response.body()!!
                    if(res.isNotEmpty()) {
                        studentsList = res
                        setTableData()
                    } else {
                        Toast.makeText(this@AttendanceTick, "Students Not Found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@AttendanceTick, "Response Error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Users>>, t: Throwable) {
                Toast.makeText(this@AttendanceTick, t.message, Toast.LENGTH_SHORT).show()
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

            // Optional: allow only one checkbox to be selected
            presentCheckBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) absentCheckBox.isChecked = false
            }
            absentCheckBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) presentCheckBox.isChecked = false
            }

            // Add views to row
            tableRow.addView(serial)
            tableRow.addView(studentName)
            tableRow.addView(presentCheckBox)
            tableRow.addView(absentCheckBox)

            // Add row to table
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
