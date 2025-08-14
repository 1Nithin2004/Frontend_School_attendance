package com.saveetha.schoolattendance.myclasses

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saveetha.schoolattendance.R
import com.saveetha.schoolattendance.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ParentAttendanceActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChildAttendanceAdapter
    private val list = mutableListOf<ChildAttendance>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_attendance)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize adapter with context + list
        adapter = ChildAttendanceAdapter(this, list)
        recyclerView.adapter = adapter

        // Get email from intent
        val parentEmail = intent.getStringExtra("email") ?: return

        fetchAttendanceForParent(parentEmail)
    }

    private fun fetchAttendanceForParent(email: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://grlp1vvl-3000.inc1.devtunnels.ms/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)

        // Step 1: Get parent name
        api.getParentNameByEmail(email).enqueue(object : Callback<ParentNameResponse> {
            override fun onResponse(call: Call<ParentNameResponse>, response: Response<ParentNameResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val parentName = response.body()!!.parentName

                    // Step 2: Fetch attendance using parent name
                    api.getParentAttendance(parentName).enqueue(object : Callback<List<ChildAttendance>> {
                        override fun onResponse(
                            call: Call<List<ChildAttendance>>,
                            response: Response<List<ChildAttendance>>
                        ) {
                            if (response.isSuccessful && response.body() != null) {
                                list.clear()
                                list.addAll(response.body()!!)
                                adapter.notifyDataSetChanged()
                            } else {
                                Toast.makeText(this@ParentAttendanceActivity, "No attendance found", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<List<ChildAttendance>>, t: Throwable) {
                            Toast.makeText(this@ParentAttendanceActivity, "Failed to load attendance", Toast.LENGTH_SHORT).show()
                        }
                    })

                } else {
                    Toast.makeText(this@ParentAttendanceActivity, "Parent not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ParentNameResponse>, t: Throwable) {
                Toast.makeText(this@ParentAttendanceActivity, "Failed to fetch parent", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
