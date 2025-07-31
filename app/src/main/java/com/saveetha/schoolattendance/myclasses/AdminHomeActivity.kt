package com.saveetha.schoolattendance.myclasses

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.saveetha.schoolattendance.MainActivity
import com.saveetha.schoolattendance.databinding.ActivityAdminHpBinding

class AdminHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminHpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminHpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logoutButton.setOnClickListener {
            showLogoutDialog()
        }

        binding.viewClassReportsButton.setOnClickListener {
            val intent = Intent(this, MyClassesActivity::class.java)
            intent.putExtra("user_type", "admin") // Pass user type as "admin"
            startActivity(intent)
        }

        binding.profileIcon.setOnClickListener {
            val adminUsername = intent.getStringExtra("ADMIN_username")
            val intent = Intent(this, AdminProfile::class.java) // If you have an AdminProfile page
            intent.putExtra("ADMIN_username", adminUsername)
            startActivity(intent)
        }
    }

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to logout?")

        builder.setPositiveButton("Yes") { _, _ ->
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }
}
