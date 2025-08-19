package com.saveetha.schoolattendance.myclasses

import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.saveetha.schoolattendance.R
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

// Data class matching your JSON
data class Attendance(
    val Full_Name: String,
    val Class: String,
    val classes_present: String,
    val classes_absent: String,
    val total: Int,
    val percentage: String
)

// Retrofit API interface
interface ApiService {
    @GET("users/parent/{email}/attendance")
    fun getAttendance(@Path("email") email: String): Call<Attendance>
}

class ParentAttendanceActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var percentText: TextView
    private lateinit var tvTotalClasses: TextView
    private lateinit var tvAttendedClasses: TextView
    private lateinit var tvAbsentClasses: TextView
    private lateinit var ivBack: ImageView
    private lateinit var tvStudentName: TextView
    private lateinit var tvClassName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_attendance)

        progressBar = findViewById(R.id.progressAttendance)
        percentText = findViewById(R.id.tvAttendancePercent)
        tvTotalClasses = findViewById(R.id.tvTotalClasses)
        tvAttendedClasses = findViewById(R.id.tvAttendedClasses)
        tvAbsentClasses = findViewById(R.id.tvAbsentClasses)
        ivBack = findViewById(R.id.ivBack)
        tvStudentName = findViewById(R.id.tvStudentName)
        tvClassName = findViewById(R.id.tvClassName)

        ivBack.setOnClickListener { finish() }

        // Get parent email dynamically from intent
        val parentEmail = intent.getStringExtra("parent_email")
        if (!parentEmail.isNullOrEmpty()) {
            fetchAttendance(parentEmail)
        } else {
            Toast.makeText(this, "Parent email not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchAttendance(email: String) {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val client = OkHttpClient.Builder().addInterceptor(logging).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://grlp1vvl-3000.inc1.devtunnels.ms/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)
        api.getAttendance(email).enqueue(object : Callback<Attendance> {
            override fun onResponse(call: Call<Attendance>, response: Response<Attendance>) {
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!
                    updateUI(data)
                } else {
                    Toast.makeText(this@ParentAttendanceActivity, "No attendance data found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Attendance>, t: Throwable) {
                Toast.makeText(this@ParentAttendanceActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUI(data: Attendance) {
        tvStudentName.text = data.Full_Name
        tvClassName.text = "Class: ${data.Class}"
        tvTotalClasses.text = "Total Classes: ${data.total}"
        tvAttendedClasses.text = "Attended Classes: ${data.classes_present}"
        tvAbsentClasses.text = "Absent Classes: ${data.classes_absent}"

        val percent = data.percentage.toFloat().toInt()
        percentText.text = "${data.percentage}%"

        // Animate progress bar
        val animator = ObjectAnimator.ofInt(progressBar, "progress", 0, percent)
        animator.duration = 1000
        animator.start()

        // Change color dynamically
        val color = if (percent >= 70) Color.GREEN else Color.RED
        progressBar.progressDrawable.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN)
    }
}
