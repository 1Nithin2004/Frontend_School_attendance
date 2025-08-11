package com.saveetha.schoolattendance.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.saveetha.schoolattendance.R
import com.saveetha.schoolattendance.myclasses.Teacher

class TeacherAdapter(
    private val teacherList: MutableList<Teacher>,
    private val onEditClick: (Teacher) -> Unit,
    private val onDeleteClick: (Teacher) -> Unit
) : RecyclerView.Adapter<TeacherAdapter.TeacherViewHolder>() {

    inner class TeacherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val teacherName: TextView = itemView.findViewById(R.id.teacherName)
        val editIcon: ImageView = itemView.findViewById(R.id.btnEdit)
        val deleteIcon: ImageView = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeacherViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_item_teacher, parent, false)
        return TeacherViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeacherViewHolder, position: Int) {
        val teacher = teacherList[position]
        holder.teacherName.text = teacher.fullName

        holder.editIcon.setOnClickListener { onEditClick(teacher) }
        holder.deleteIcon.setOnClickListener { onDeleteClick(teacher) }
    }

    override fun getItemCount(): Int = teacherList.size
    fun updateData(newList: List<Teacher>) {
        teacherList.clear()
        teacherList.addAll(newList)
        notifyDataSetChanged()
    }
}
