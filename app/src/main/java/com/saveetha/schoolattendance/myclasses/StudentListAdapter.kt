package com.saveetha.schoolattendance.myclasses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.saveetha.schoolattendance.R
import com.saveetha.schoolattendance.service.ApiService
import com.saveetha.schoolattendance.service.RetroFit
import retrofit2.Call
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import retrofit2.Callback
import retrofit2.Response

class StudentListAdapter(
    private val context: Context,
    private var studentList: MutableList<Student>,
    private val refreshCallback: () -> Unit
) : RecyclerView.Adapter<StudentListAdapter.StudentViewHolder>() {


    class StudentViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.studentName)
        val edit = view.findViewById<ImageView>(R.id.editIcon)
        val delete = view.findViewById<ImageView>(R.id.deleteIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun getItemCount() = studentList.size

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList[position]
        holder.name.text = student.fullName

        holder.edit.setOnClickListener {
            val intent = Intent(context, UpdateStudentActivity::class.java)
            intent.putExtra("studentId", student.id)
            intent.putExtra("studentName", student.fullName)
            intent.putExtra("studentEmail", student.email)
            intent.putExtra("studentPhone", student.phoneNumber.toString()) // convert Long to String
            intent.putExtra("studentPassword", student.password)
            intent.putExtra("studentClass", student.studentClass)
            intent.putExtra("studentDob", student.dob)
            intent.putExtra("parentName", student.parentsName)
            context.startActivity(intent)
        }


        holder.delete.setOnClickListener {
            val studentName = studentList[position].fullName
            val studentId = studentList[position].id

            AlertDialog.Builder(context)
                .setTitle("Delete Confirmation")
                .setMessage("Are you sure you want to delete $studentName?")
                .setPositiveButton("Delete") { _, _ ->
                    deleteStudent(studentId, position)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun deleteStudent(studentId: Int, position: Int) {
        val apiService = RetroFit.getService()
        apiService.deleteStudent(studentId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    studentList.removeAt(position)
                    notifyItemRemoved(position)
                    Toast.makeText(context, "Student deleted", Toast.LENGTH_SHORT).show()
                    refreshCallback()
                } else {
                    Toast.makeText(context, "Delete failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
