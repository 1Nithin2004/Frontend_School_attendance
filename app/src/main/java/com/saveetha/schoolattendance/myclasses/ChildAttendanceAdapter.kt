package com.saveetha.schoolattendance.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.saveetha.schoolattendance.R
import com.saveetha.schoolattendance.myclasses.ChildAttendance
import com.github.lzyzsd.circleprogress.DonutProgress

class ChildAttendanceAdapter(
    private val studentList: List<ChildAttendance>
) : RecyclerView.Adapter<ChildAttendanceAdapter.AttendanceViewHolder>() {

    class AttendanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvStudentName: TextView = itemView.findViewById(R.id.tvStudentName)
        val circularProgress: DonutProgress = itemView.findViewById(R.id.circularProgress)
        val tvTotal: TextView = itemView.findViewById(R.id.tvTotalValue)
        val tvPresent: TextView = itemView.findViewById(R.id.tvPresentValue)
        val tvAbsent: TextView = itemView.findViewById(R.id.tvAbsentValue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_child_attendance, parent, false)
        return AttendanceViewHolder(view)
    }

    override fun getItemCount(): Int = studentList.size

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        val student = studentList[position]

        // Student name
        holder.tvStudentName.text = student.name

        // Attendance values
        holder.tvTotal.text = student.total_days.toString()
        holder.tvPresent.text = student.present_days.toString()
        holder.tvAbsent.text = student.absent_days.toString()

        // Attendance percentage
        val percentage = student.attendance_percentage.toFloatOrNull() ?: 0f

        // Determine color
        val color = when (student.status.lowercase()) {
            "green" -> Color.GREEN
            "yellow" -> Color.YELLOW
            "red" -> Color.RED
            else -> Color.GRAY
        }

        holder.circularProgress.apply {
            finishedStrokeColor = color
            unfinishedStrokeColor = Color.LTGRAY
            progress = percentage         // sets the donut progress
            text = "${percentage.toInt()}%"
        }

    }
}
