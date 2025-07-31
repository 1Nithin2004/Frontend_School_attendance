package com.saveetha.schoolattendance.myclasses

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.saveetha.schoolattendance.databinding.ActivityAdminProfileBinding

class AdminProfile : AppCompatActivity() {

    private lateinit var binding: ActivityAdminProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Static Admin Details
        binding.tvName.text = "Siva Kumar V"
        binding.tvEmail.text = "Administrator"
        binding.tvDob.text = "+91 9876543210"
        binding.tvClasses.text = "admin@school.com"

        // Back arrow functionality (optional)
        binding.backArrow.setOnClickListener {
            finish() // Simply go back to the previous screen
        }
    }
}
