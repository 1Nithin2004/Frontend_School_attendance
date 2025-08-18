package com.saveetha.schoolattendance.myclasses

import android.os.Bundle
import android.util.Log
import android.widget.Toast
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

        // Get USER_ID from intent
        parentId = intent.getStringExtra("USER_ID")

        binding.backArrow.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        if (!parentId.isNullOrEmpty()) {
            loadParentProfile(parentId!!)
        } else {
            Toast.makeText(this, "Parent ID not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadParentProfile(id: String) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://grlp1vvl-3000.inc1.devtunnels.ms/users/$id") // Make sure this matches backend route
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@ParentProfile, "Failed to load profile", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    try {
                        val obj = JSONObject(responseData ?: "{}")
                        Log.d("API_RESPONSE", responseData ?: "null")

                        runOnUiThread {
                            binding.tvName.text = obj.optString("Full_Name", "N/A")
                            binding.tvPhone.text = obj.optString("Phone_Number", "N/A")
                            binding.tvEmail.text = obj.optString("email_address", "N/A")
                            binding.tvDob.text = obj.optString("Date_of_Birth", "N/A")
                            binding.tvClasses.text = obj.optString("Class", "N/A")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        runOnUiThread {
                            Toast.makeText(this@ParentProfile, "Error parsing profile data", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@ParentProfile, "Error: ${response.code}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
