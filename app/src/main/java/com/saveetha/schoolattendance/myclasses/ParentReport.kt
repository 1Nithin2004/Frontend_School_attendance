package com.saveetha.schoolattendance.myclasses

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.saveetha.schoolattendance.databinding.ActivityParentReportBinding

class ParentReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityParentReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParentReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // **This is where you would make your API call to get the data**
        // Since each parent has only one child, you would get this data
        // as soon as they log in or navigate to this screen.
        val attendancePercentage = 80
        val totalDays = 80
        val presentDays = 64
        val absentDays = 16

        // Set the Progress Bar
        binding.attendanceProgressBar.progress = attendancePercentage
        binding.percentageTextView.text = "$attendancePercentage%"

        // Set the TextViews in the summary card
        binding.totalDaysTextView.text = totalDays.toString()
        binding.presentDaysTextView.text = presentDays.toString()
        binding.absentDaysTextView.text = absentDays.toString()

        // Set the colors for the text
        binding.presentDaysTextView.setTextColor(Color.parseColor("#4CAF50"))
        binding.absentDaysTextView.setTextColor(Color.parseColor("#F44336"))
    }
}