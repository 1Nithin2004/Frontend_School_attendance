package com.saveetha.schoolattendance.myclasses

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.saveetha.schoolattendance.R
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class TeacherProfile : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvDob: TextView
    private lateinit var tvClasses: TextView
    private var teacherId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_profile)

        // Action bar back button & title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Profile Details"

        // Initialize views
        tvName = findViewById(R.id.tvName)
        tvPhone = findViewById(R.id.tvPhone)
        tvEmail = findViewById(R.id.tvEmail)
        tvDob = findViewById(R.id.tvDob)
        tvClasses = findViewById(R.id.tvClasses)

        // Get teacher ID from intent
        teacherId = intent.getStringExtra("USER_ID")

        if (!teacherId.isNullOrEmpty()) {
            loadTeacherProfile(teacherId!!)
        } else {
            Toast.makeText(this, "Teacher ID not found", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun loadTeacherProfile(id: String) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://grlp1vvl-3000.inc1.devtunnels.ms/users/teachers/$id")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@TeacherProfile, "Failed to load profile", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    try {
                        val obj = JSONObject(responseData ?: "{}")
                        Log.d("API_RESPONSE", responseData ?: "null")

                        runOnUiThread {
                            tvName.text = " ${obj.optString("Full_Name", "N/A")}"
                            tvPhone.text = " ${obj.optString("Phone_Number", "N/A")}"
                            tvEmail.text = " ${obj.optString("email_address", "N/A")}"
                            tvDob.text = " ${obj.optString("Date_of_Birth", "N/A")}"
                            tvClasses.text = " ${obj.optString("Class", "N/A")}"
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        runOnUiThread {
                            Toast.makeText(this@TeacherProfile, "Error parsing profile data", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@TeacherProfile, "Error: ${response.code}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
