package com.saveetha.schoolattendance.myclasses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.saveetha.schoolattendance.R

class StudentListAdapter(private val students: List<Student>) :
    RecyclerView.Adapter<StudentListAdapter.StudentViewHolder>() {

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

    override fun getItemCount() = students.size

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        holder.name.text = student.fullName

        holder.edit.setOnClickListener {
            // TODO: Handle edit logic
        }

        holder.delete.setOnClickListener {
            // TODO: Handle delete logic
        }
    }
}
