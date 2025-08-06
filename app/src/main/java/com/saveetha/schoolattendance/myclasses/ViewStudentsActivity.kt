package com.saveetha.schoolattendance.myclasses

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.saveetha.schoolattendance.databinding.ActivityViewStudentsBinding
import com.saveetha.schoolattendance.service.RetroFit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log

class ViewStudentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewStudentsBinding
    private lateinit var className: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewStudentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        className = intent.getStringExtra("class_name") ?: ""

        binding.title.text = "Students of Class $className"
        binding.backArrow.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        fetchStudents()
    }

    private fun fetchStudents() {
        // Removed progressBar references
        val className = intent.getStringExtra("class_name") ?: "6"
        val classId = className.toIntOrNull() ?: 6 // fallback to 6 if conversion fails

        RetroFit.getService().getStudentsByClass(classId).enqueue(object : Callback<List<Student>> {
            override fun onResponse(call: Call<List<Student>>, response: Response<List<Student>>) {
                if (response.isSuccessful) {
                    val students = response.body() ?: emptyList()
                    Log.d("STUDENTS", "Received students: $students")

                    val adapter = StudentListAdapter(students)
                    binding.studentRecyclerView.adapter = adapter
                    binding.studentRecyclerView.layoutManager = LinearLayoutManager(this@ViewStudentsActivity)
                    binding.studentRecyclerView.visibility = View.VISIBLE
                } else {
                    Toast.makeText(this@ViewStudentsActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Student>>, t: Throwable) {
                Toast.makeText(this@ViewStudentsActivity, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }

}
