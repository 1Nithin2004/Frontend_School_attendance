package com.saveetha.schoolattendance.myclasses

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
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

        // Edge-to-edge support
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            enableEdgeToEdge()
            ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

        setupClickListeners()
    }

    /** Setup click listeners for cards and icons */
    private fun setupClickListeners() {
        // Get parent name from SharedPreferences
        val sharedPrefs = getSharedPreferences("login", Context.MODE_PRIVATE)
        val parentName = sharedPrefs.getString("parent_name", "") ?: ""

        // Logout button
        binding.logout.setOnClickListener {
            showLogoutDialog()
        }

        // Attendance card click
        // In ParentHomepageActivity
        binding.attendanceCard.setOnClickListener {
            // Get parent name from SharedPreferences
            val sharedPrefs = getSharedPreferences("login", Context.MODE_PRIVATE)
            val parentName = sharedPrefs.getString("parent_name", "") ?: ""

            if (parentName.isNotEmpty()) {
                // Pass parentName to ParentAttendanceActivity
                val intent = Intent(this, ParentAttendanceActivity::class.java)
                intent.putExtra("PARENT_NAME", parentName)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Parent name not found", Toast.LENGTH_SHORT).show()
            }
        }



        binding.profileIcon.setOnClickListener {
            val sharedPrefs = getSharedPreferences("login", Context.MODE_PRIVATE)
            val userId = sharedPrefs.getInt("user_id", 0)

            if (userId != 0) {
                val intent = Intent(this, ParentProfile::class.java)
                intent.putExtra("USER_ID", userId.toString())
                startActivity(intent)
            } else {
                Toast.makeText(this, "Parent ID not found", Toast.LENGTH_SHORT).show()
            }
        }

    }

    /** Logout confirmation dialog */
    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                finish() // End activity, return to login screen
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}
