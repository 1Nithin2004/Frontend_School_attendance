package com.saveetha.schoolattendance.myclasses

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.saveetha.schoolattendance.databinding.ActivityMyClassesBinding
import com.saveetha.schoolattendance.databinding.ActivitySelectedclassBinding

class MarkorReportActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySelectedclassBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectedclassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val receivedClassName = intent.getStringExtra("class_name") ?: "No class"
        binding.classNametextview.text = "Class "+receivedClassName

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
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
        binding.clickbutton.setOnClickListener{
            val intent = Intent(this, AttendanceTick::class.java)
            intent.putExtra("class",receivedClassName )
            startActivity(intent)
        }
        binding.clickreport.setOnClickListener {
            val intent = Intent(this, StudentReportActivity::class.java)
            intent.putExtra("classId", receivedClassName) // send the class name/id
            startActivity(intent)
        }
    }
}