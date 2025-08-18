package com.saveetha.schoolattendance.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import com.saveetha.schoolattendance.R
import com.saveetha.schoolattendance.myclasses.ChildAttendance

class ChildAttendanceAdapter(
    private val studentList: List<ChildAttendance>
) : RecyclerView.Adapter<ChildAttendanceAdapter.AttendanceViewHolder>() {

    class AttendanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvStudentName: TextView = itemView.findViewById(R.id.tvStudentName)
        val circularProgressBar: CircularProgressBar = itemView.findViewById(R.id.circularProgressBar)
        val tvTotal: TextView = itemView.findViewById(R.id.tvTotal)
        val tvPresent: TextView = itemView.findViewById(R.id.tvPresent)
        val tvAbsent: TextView = itemView.findViewById(R.id.tvAbsent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_child_attendance, parent, false)
        return AttendanceViewHolder(view)
    }

    override fun getItemCount(): Int = studentList.size

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        val student = studentList[position]

        // Set student name
        holder.tvStudentName.text = student.name

        // Set attendance texts
        holder.tvTotal.text = "Total Days: ${student.total_days}"
        holder.tvPresent.text = "Present Days: ${student.present_days}"
        holder.tvAbsent.text = "Absent Days: ${student.absent_days}"

        // Calculate percentage (float)
        val attendancePercentage = student.attendance_percentage.toFloatOrNull() ?: 0f

        // Configure circular progress bar
        holder.circularProgressBar.apply {
            progressBarWidth = 8f
            backgroundProgressBarWidth = 4f
            progress = attendancePercentage
            progressBarColor = when (student.status.lowercase()) {
                "green" -> Color.GREEN
                "yellow" -> Color.YELLOW
                "red" -> Color.RED
                else -> Color.GRAY
            }
        }
    }
}
