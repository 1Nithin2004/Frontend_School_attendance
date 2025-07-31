package com.saveetha.schoolattendance.myclasses

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.saveetha.schoolattendance.databinding.ActivityParentProfileBinding
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class ParentProfile : AppCompatActivity() {

    private lateinit var binding: ActivityParentProfileBinding
    private var parentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParentProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Profile Details"

        parentId = intent.getStringExtra("PARENT_username")

        // Handle back arrow
        binding.backArrow.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        if (!parentId.isNullOrEmpty()) {
            loadParentProfile(parentId!!)
        }
    }

    private fun loadParentProfile(id: String) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://your-server.com/api/parent/$id") // 🔁 Replace with actual URL
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    try {
                        val obj = JSONObject(responseData)
                        runOnUiThread {
                            binding.tvName.text = "Name: ${obj.getString("full_name")}"
                            binding.tvPhone.text = "Phone: ${obj.getString("phone_number")}"
                            binding.tvEmail.text = "Email: ${obj.getString("email_address")}"
                            binding.tvDob.text = "DOB: ${obj.getString("date_of_birth")}"
                            binding.tvClasses.text = "Child Class: ${obj.getString("class")}" // Adjust key if needed
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        })
    }
}
