package com.saveetha.schoolattendance.myclasses

import android.os.Bundle
import android.widget.Toast
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
        teacherAdapter = TeacherAdapter(listOf(), onEditClick = { teacher ->
            // TODO: Handle edit click
        }, onDeleteClick = { teacher ->
            // TODO: Handle delete click
        })

        binding.teacherRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.teacherRecyclerView.adapter = teacherAdapter
    }

    private fun loadTeachersFromServer() {
        RetroFit.getService().getTeachers().enqueue(object : Callback<List<Teacher>> {
            override fun onResponse(call: Call<List<Teacher>>, response: Response<List<Teacher>>) {
                if (response.isSuccessful && response.body() != null) {
                    teacherAdapter = TeacherAdapter(
                        response.body()!!,
                        onEditClick = { teacher ->
                            // TODO: Handle edit click
                        },
                        onDeleteClick = { teacher ->
                            // TODO: Handle delete click
                        }
                    )
                    binding.teacherRecyclerView.adapter = teacherAdapter
                } else {
                    Toast.makeText(this@MyTeachersActivity, "No teachers found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Teacher>>, t: Throwable) {
                Toast.makeText(this@MyTeachersActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
