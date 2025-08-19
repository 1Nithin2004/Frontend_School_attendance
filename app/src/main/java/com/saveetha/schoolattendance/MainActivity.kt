package com.saveetha.schoolattendance

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.saveetha.schoolattendance.databinding.ActivityMainBinding
import com.saveetha.schoolattendance.myclasses.AdminHomeActivity
import com.saveetha.schoolattendance.service.RetroFit
import com.saveetha.schoolattendance.service.request.LoginRequest
import com.saveetha.schoolattendance.service.response.LoginResponse
import com.saveetha.schoolattendance.myclasses.ParentHomepageActivity
import com.saveetha.schoolattendance.teacherhomepage.TeacherHomepageActivity
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    private lateinit var sf:SharedPreferences
    private lateinit var edit:SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Check if already logged in. This block is for initial launch.
        // It should also pass user type and ID if already logged in. *

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sf = getSharedPreferences("login", Context.MODE_PRIVATE)
        edit = sf.edit() // Initialize editor here, as it's used later

        // No need for this block if the 'isLoggedIn' check at the top handles it.
        // This 'if (sf.getInt("user_id", 0)!=0)' duplicates the initial check.
        // I'm commenting it out, assuming the first 'if (isLoggedIn)' block is sufficient.
        /*
        if(sf.getInt("user_id", 0)!=0) {
            val userType = sf.getString("user_type", null).toString()
            if(userType=="Teacher") {
                val Teacherhomepage = Intent(this@MainActivity,TeacherHomepageActivity::class.java)
                startActivity(Teacherhomepage)
            } else if(userType=="Parent") {

            } else {

            }
            return
        }
        */

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
                RetroFit.getService().login(LoginRequest(email= username, password = password)).enqueue(object:Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        binding.loginbutton.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        if(response.isSuccessful) {
                            Toast.makeText(this@MainActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()

                            // Store all relevant user data in SharedPreferences
                            val userId = response.body()!!.user.Id
                            val userType = response.body()!!.user.User_type
//                            val userNameFromResponse = response.body()!!.user.username // Assuming this field exists in your LoginResponse.User
                            // You might also want to store other common fields if they exist and are useful
                            // val userEmail = response.body()!!.user.email // if different from username
                            val userFullName = response.body()!!.user.Full_Name

                            edit.putInt("user_id", userId).apply()
                            edit.putString("user_type", userType).apply()
                            edit.putString("username", username).apply()
                            edit.putString("parent_name", userFullName).apply()

                            if (userType == "Teacher") {
                                val teacherHomepageIntent = Intent(this@MainActivity, TeacherHomepageActivity::class.java).apply {
                                    putExtra("USERNAME", username)
                                    putExtra("TEACHER_ID" , userId.toString())
                                }
                                startActivity(teacherHomepageIntent)
                                finish()
                            }else if(userType=="Parent") {
                                val userEmail = response.body()!!.user.email_address
                                edit.putString("parent_email", userEmail).apply()
                                edit.putString("parent_name", userFullName).apply()
                                edit.putInt("user_id", userId).apply()

                                val ParentHomepage = Intent(this@MainActivity, ParentHomepageActivity::class.java).apply {
                                    putExtra("USER_ID", userId.toString())
                                }
                                startActivity(ParentHomepage)
                                finish()
                     } else if (userType == "admin") {
                                val adminIntent = Intent(this@MainActivity, AdminHomeActivity::class.java).apply {
                                    putExtra("ADMIN_username", username)
                                }
                                startActivity(adminIntent)
                            } else {
                                // Handle unknown user types
                                Toast.makeText(this@MainActivity, "Unknown user type: $userType. Please contact support.", Toast.LENGTH_LONG).show()
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