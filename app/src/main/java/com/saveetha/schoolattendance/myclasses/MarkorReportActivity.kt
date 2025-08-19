package com.saveetha.schoolattendance.myclasses

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.saveetha.schoolattendance.R
import com.saveetha.schoolattendance.databinding.ActivitySelectedclassBinding

class MarkorReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectedclassBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectedclassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load class info and setup UI
        handleIntent(intent)

        val backArrow = findViewById<ImageView>(R.id.backArrow)
        backArrow.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            enableEdgeToEdge()
            ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
                val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                view.setPadding(
                    systemBarsInsets.left,
                    systemBarsInsets.top,
                    systemBarsInsets.right,
                    systemBarsInsets.bottom
                )
                WindowInsetsCompat.CONSUMED
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    // Centralized method to handle intent and setup UI
    private fun handleIntent(intent: Intent) {
        val receivedClassName = intent.getStringExtra("class_name") ?: "No class"
        binding.classNametextview.text = "Class $receivedClassName"

        val userType = intent.getStringExtra("user_type")
        if (userType == "parent") {
            showReportsOnly()
        } else {
            showMarkOrViewOptions()
        }

        // Set click listeners with updated class
        binding.clickbutton.setOnClickListener {
            val attendanceIntent = Intent(this, AttendanceTick::class.java)
            attendanceIntent.putExtra("class", receivedClassName)
            startActivity(attendanceIntent)
        }

        binding.clickreport.setOnClickListener {
            val reportIntent = Intent(this, StudentReportActivity::class.java)
            reportIntent.putExtra("classId", receivedClassName)
            startActivity(reportIntent)
        }
    }

    private fun showReportsOnly() {
        binding.cardMarkAttendance.visibility = View.GONE
        binding.cardViewReports.visibility = View.VISIBLE
    }

    private fun showMarkOrViewOptions() {
        binding.cardMarkAttendance.visibility = View.VISIBLE
        binding.cardViewReports.visibility = View.VISIBLE
    }
}
