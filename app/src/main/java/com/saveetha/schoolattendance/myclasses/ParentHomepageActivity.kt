package com.saveetha.schoolattendance.myclasses

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.saveetha.schoolattendance.databinding.ActivityParentHomepageBinding

class ParentHomepageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityParentHomepageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParentHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val parentId = intent.getStringExtra("USER_ID")

        // Edge-to-edge support
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            enableEdgeToEdge()
            ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

        setupClickListeners(parentId)
    }

    /** Setup click listeners for cards and icons */
    private fun setupClickListeners(parentId: String?) {

        // Logout button
        binding.logout.setOnClickListener {
            showLogoutDialog()
        }

        // Attendance card click
        binding.attendanceCard.setOnClickListener {
            val intent = Intent(this, ParentAttendanceActivity::class.java)
            intent.putExtra("USER_ID", parentId)
            startActivity(intent)
        }

        // Profile icon click
        binding.profileIcon.setOnClickListener {
            val intent = Intent(this, ParentProfile::class.java)
            intent.putExtra("USER_ID", parentId)
            startActivity(intent)
        }
    }

    /** Logout confirmation dialog */
    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                finish() // End activity, return to previous screen or login
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}
