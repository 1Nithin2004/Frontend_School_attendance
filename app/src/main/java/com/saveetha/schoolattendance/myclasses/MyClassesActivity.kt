package com.saveetha.schoolattendance.myclasses

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.saveetha.schoolattendance.databinding.ActivityMyClassesBinding
import com.saveetha.schoolattendance.service.RetroFit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyClassesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyClassesBinding
    private var userType: String = "teacher"  // default to teacher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyClassesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set user type from intent
        userType = intent.getStringExtra("user_type") ?: "teacher"

        // Back arrow
        binding.backArrow.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fetchClasses()  // Call after userType is set
    }

    private fun fetchClasses() {
        binding.progressBar.visibility = View.VISIBLE
        binding.classListRV.visibility = View.GONE

        RetroFit.getService().getClasses().enqueue(object : Callback<List<MyClasses>> {
            override fun onResponse(call: Call<List<MyClasses>>, response: Response<List<MyClasses>>) {
                binding.progressBar.visibility = View.GONE
                binding.classListRV.visibility = View.VISIBLE

                if (response.isSuccessful) {
                    val classList = response.body() ?: emptyList()
                    val adapter = ClassesAdapter(classList, this@MyClassesActivity)

                    adapter.setupclicklistener(object : ClassesAdapter.ClickListener {
                        override fun click(data: MyClasses) {
                            val intent = Intent(this@MyClassesActivity, MarkorReportActivity::class.java)
                            intent.putExtra("class_name", data.class_name)
                            intent.putExtra("user_type", userType)
                            startActivity(intent)
                        }
                    })

                    binding.classListRV.adapter = adapter
                    binding.classListRV.layoutManager = LinearLayoutManager(this@MyClassesActivity)

                } else {
                    Toast.makeText(this@MyClassesActivity, "Failed: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<MyClasses>>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                binding.classListRV.visibility = View.VISIBLE
                Toast.makeText(this@MyClassesActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
