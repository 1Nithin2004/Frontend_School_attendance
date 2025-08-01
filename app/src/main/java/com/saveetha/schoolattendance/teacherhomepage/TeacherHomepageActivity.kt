package com.saveetha.schoolattendance.teacherhomepage

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.saveetha.schoolattendance.MainActivity
import com.saveetha.schoolattendance.R
import com.saveetha.schoolattendance.databinding.ActivityTeacherHomepageBinding
import com.saveetha.schoolattendance.databinding.ActivityTeacherProfileBinding
import com.saveetha.schoolattendance.myclasses.MyClassesActivity
import com.saveetha.schoolattendance.myclasses.TeacherProfile

//import com.saveetha.schoolattendance.myclasses.MyClassesActivity

class TeacherHomepageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeacherHomepageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTeacherHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val teacherId = intent.getStringExtra("TEACHER_ID")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            enableEdgeToEdge()
            ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }
        binding.logout.setOnClickListener{
            showLogoutDialog()
        }
        // ✅ Click listener to navigate to MyClassesActivity
        binding.attendanceCard.setOnClickListener {
            val intent = Intent(this, MyClassesActivity::class.java)
            startActivity(intent)
        }

        binding.profileIcon.setOnClickListener {
            val teacherId = intent.getStringExtra("TEACHER_ID")
            val intent = Intent(this, TeacherProfile::class.java)
            intent.putExtra("TEACHER_ID", teacherId)
            startActivity(intent)
        }
        val prefs = getSharedPreferences("login", Context.MODE_PRIVATE)
        val email = prefs.getString("username", "Not available")

// Example: Show email in a TextView
//        findViewById<TextView>(R.id.profileEmailTextView).text = "Email: $email"



    }
    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to logout?")

        builder.setPositiveButton("Yes") { _, _ ->
            // Navigate to LoginActivity
//            val intent = Intent(this, MainActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
            finish()
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }
}
