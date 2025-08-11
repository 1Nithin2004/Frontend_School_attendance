package com.saveetha.schoolattendance.myclasses

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.saveetha.schoolattendance.adapter.TeacherAdapter
import com.saveetha.schoolattendance.databinding.ActivityEditTeachersBinding
import com.saveetha.schoolattendance.service.RetroFit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyTeachersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditTeachersBinding
    private lateinit var teacherAdapter: TeacherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTeachersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadTeachersFromServer()
    }

    private fun setupRecyclerView() {
        teacherAdapter = TeacherAdapter(
            teacherList = mutableListOf(),
            onEditClick = { teacher ->
                val i = Intent(this, UpdateTeachersActivity::class.java)
                i.putExtra("teacher_id", teacher.id)
                startActivity(i)
            },
            onDeleteClick = { teacher ->
                AlertDialog.Builder(this)
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure you want to delete ${teacher.fullName}?")
                    .setPositiveButton("Delete") { _, _ ->
                        deleteTeacherFromServer(teacher.id)
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        )

        binding.teacherRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.teacherRecyclerView.adapter = teacherAdapter
    }

    private fun loadTeachersFromServer() {
        RetroFit.getService().getTeachers().enqueue(object : Callback<List<Teacher>> {
            override fun onResponse(call: Call<List<Teacher>>, response: Response<List<Teacher>>) {
                if (response.isSuccessful && response.body() != null) {
                    teacherAdapter.updateData(response.body()!!)
                } else {
                    Toast.makeText(this@MyTeachersActivity, "No teachers found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Teacher>>, t: Throwable) {
                Toast.makeText(this@MyTeachersActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteTeacherFromServer(teacherId: Int) {
        RetroFit.getService().deleteTeacher(teacherId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MyTeachersActivity, "Teacher deleted successfully", Toast.LENGTH_SHORT).show()
                    loadTeachersFromServer()
                } else {
                    Toast.makeText(this@MyTeachersActivity, "Failed to delete teacher", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@MyTeachersActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
