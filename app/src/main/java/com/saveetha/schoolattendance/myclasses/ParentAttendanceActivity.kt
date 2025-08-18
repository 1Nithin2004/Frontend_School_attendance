package com.saveetha.schoolattendance.myclasses

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saveetha.schoolattendance.R
import com.saveetha.schoolattendance.adapter.ChildAttendanceAdapter
import com.saveetha.schoolattendance.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ParentAttendanceActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChildAttendanceAdapter
    private var studentList = mutableListOf<ChildAttendance>()
    private lateinit var tvEmpty: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_attendance)

        recyclerView = findViewById(R.id.recyclerViewAttendance)
        tvEmpty = findViewById(R.id.tvEmptyAttendance)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ChildAttendanceAdapter(studentList)
        recyclerView.adapter = adapter

        // Get parent name from SharedPreferences
        val parentName = getSharedPreferences("login", MODE_PRIVATE)
            .getString("parent_name", "")?.trim() ?: ""

        Log.d("ParentAttendance", "Parent name from SharedPreferences: '$parentName'")

        if (parentName.isNotEmpty()) {
            fetchAttendance(parentName)
        } else {
            tvEmpty.text = "Parent name not found"
            tvEmpty.visibility = View.VISIBLE
            Toast.makeText(this, "Parent name not provided", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchAttendance(parentName: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://grlp1vvl-3000.inc1.devtunnels.ms/") // Base URL only
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)

        Log.d("ParentAttendance", "Calling API for parent: $parentName")

        api.getParentAttendance(parentName).enqueue(object : Callback<List<ChildAttendance>> {
            override fun onResponse(
                call: Call<List<ChildAttendance>>,
                response: Response<List<ChildAttendance>>
            ) {
                Log.d("ParentAttendance", "Response code: ${response.code()}")
                Log.d("ParentAttendance", "Response body: ${response.body()}")

                if (response.isSuccessful) {
                    val data = response.body()
                    if (!data.isNullOrEmpty()) {
                        studentList.clear()
                        studentList.addAll(data)
                        adapter.notifyDataSetChanged()
                        tvEmpty.visibility = View.GONE
                    } else {
                        tvEmpty.text = "No attendance data found"
                        tvEmpty.visibility = View.VISIBLE
                    }
                } else {
                    tvEmpty.text = "Failed to fetch attendance"
                    tvEmpty.visibility = View.VISIBLE
                    Log.e("ParentAttendance", "API error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<ChildAttendance>>, t: Throwable) {
                tvEmpty.text = "Error: ${t.message}"
                tvEmpty.visibility = View.VISIBLE
                Log.e("ParentAttendance", "API call failed", t)
            }
        })
    }
}
