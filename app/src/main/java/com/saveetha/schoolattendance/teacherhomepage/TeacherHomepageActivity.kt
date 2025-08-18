package com.saveetha.schoolattendance.teacherhomepage

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.saveetha.schoolattendance.MainActivity
import com.saveetha.schoolattendance.databinding.ActivityTeacherHomepageBinding
import com.saveetha.schoolattendance.myclasses.MyClassesActivity
import com.saveetha.schoolattendance.myclasses.TeacherProfile

class TeacherHomepageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeacherHomepageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val teacherId = intent.getStringExtra("TEACHER_ID")

        setupCalendar()
        setupEdgeToEdge()
        setupClickListeners(teacherId)
    }

    /** Setup MaterialCalendarView */
    private fun setupCalendar() {
        binding.calendarView.setOnDateChangedListener(OnDateSelectedListener { _, _, _ ->
            // No toast, just handle selection internally if needed
        })
    }

    /** Enable edge-to-edge display for Android 15+ */
    private fun setupEdgeToEdge() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            enableEdgeToEdge()
            ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }
    }

    /** Setup all click listeners */
    private fun setupClickListeners(teacherId: String?) {
        // Logout button
        binding.logout.setOnClickListener { showLogoutDialog() }

        // Attendance card click
        binding.attendanceCard.setOnClickListener {
            val intent = Intent(this, MyClassesActivity::class.java)
            intent.putExtra("source", "mark_attendance")
            intent.putExtra("user_type", "teacher")
            startActivity(intent)
        }

        // Profile icon click
        binding.profileIcon.setOnClickListener {
            val intent = Intent(this, TeacherProfile::class.java)
            intent.putExtra("USER_ID", teacherId)
            startActivity(intent)
        }

        // Example: get email from SharedPreferences
        val prefs = getSharedPreferences("login", Context.MODE_PRIVATE)
        val email = prefs.getString("username", "Not available")
        // You can use this email to display in the profile if needed
    }

    /** Show logout confirmation dialog */
    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }
}
