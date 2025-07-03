package com.saveetha.schoolattendance

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.saveetha.schoolattendance.databinding.ActivityMainBinding
import com.saveetha.schoolattendance.service.RetroFit
import com.saveetha.schoolattendance.service.request.LoginRequest
import com.saveetha.schoolattendance.service.response.LoginResponse
import com.saveetha.schoolattendance.teacherhomepage.TeacherHomepageActivity
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val isLoggedIn = getSharedPreferences("MyPrefs", MODE_PRIVATE).getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            startActivity(Intent(this, TeacherHomepageActivity::class.java))
            finish()
            return
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.loginbutton.setOnClickListener {
            val username = binding.accessibilitycustomaction5.text.toString().trim()
            val password = binding.accessibilitycustomaction6.text.toString().trim()

            var isValid = true

            if (username.isEmpty()) {
                binding.accessibilitycustomaction5.error = "Username is required"
                isValid = false
            }

            if (password.isEmpty()) {
                binding.accessibilitycustomaction6.error = "Password is required"
                isValid = false
            }

            if (isValid) {
                // Proceed with login API call
                binding.loginbutton.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
                RetroFit.getService().login(LoginRequest(email_address = username, password = password)).enqueue(object:Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        binding.loginbutton.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        if(response.isSuccessful) {
                            Toast.makeText(this@MainActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                            if(response.body()!!.user.User_type=="Teacher") {
                                val Teacherhomepage = Intent(this@MainActivity,TeacherHomepageActivity::class.java)
                                startActivity(Teacherhomepage)
                            } else if(response.body()!!.user.User_type=="Parent") {

                            } else {

                            }
                        } else {
                            try {
                                val res = JSONObject(response.errorBody()!!.string())
                                Toast.makeText(this@MainActivity, res.getString("message"), Toast.LENGTH_SHORT).show()
                            } catch (e:Exception) {
                                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }


                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        binding.loginbutton.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }

    }
}