import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class ProfileActivity : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvDob: TextView
    private lateinit var tvClasses: TextView
    private var teacherId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        tvName = findViewById(R.id.tvName)
        tvPhone = findViewById(R.id.tvPhone)
        tvEmail = findViewById(R.id.tvEmail)
        tvDob = findViewById(R.id.tvDob)
        tvClasses = findViewById(R.id.tvClasses)

        teacherId = intent.getStringExtra("teacher_id")

        if (!teacherId.isNullOrEmpty()) {
            loadTeacherProfile(teacherId!!)
        }
    }

    private fun loadTeacherProfile(id: String) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://your-server.com/api/teacher/$id")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    try {
                        val obj = JSONObject(responseData)

                        runOnUiThread {
                            tvName.text = "Name: ${obj.getString("full_name")}"
                            tvPhone.text = "Phone: ${obj.getString("phone_number")}"
                            tvEmail.text = "Email: ${obj.getString("email_address")}"
                            tvDob.text = "DOB: ${obj.getString("date_of_birth")}"
                            tvClasses.text = "Classes: ${obj.getString("class")}"
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        })
    }
}
