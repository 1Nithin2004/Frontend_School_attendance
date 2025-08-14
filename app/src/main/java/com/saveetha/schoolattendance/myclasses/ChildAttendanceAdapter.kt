package com.saveetha.schoolattendance.myclasses

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import com.saveetha.schoolattendance.R

class ChildAttendanceAdapter(
    private val context: Context,
    private val list: List<ChildAttendance>
) : RecyclerView.Adapter<ChildAttendanceAdapter.AttendanceViewHolder>() {

    inner class AttendanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val circularProgressBar: CircularProgressBar = itemView.findViewById(R.id.circularProgressBar)
        val tvStudentName: TextView = itemView.findViewById(R.id.tvStudentName)
        val tvTotalDays: TextView = itemView.findViewById(R.id.tvTotal)
        val tvPresentDays: TextView = itemView.findViewById(R.id.tvPresent)
        val tvAbsentDays: TextView = itemView.findViewById(R.id.tvAbsent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_child_attendance, parent, false)
        return AttendanceViewHolder(view)
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        val child = list[position]

        holder.tvStudentName.text = child.name
        holder.tvTotalDays.text = "Total: ${child.total_days}"
        holder.tvPresentDays.text = "Present: ${child.present_days}"
        holder.tvAbsentDays.text = "Absent: ${child.absent_days}"

        val percentage = child.attendance_percentage.toFloat()
        holder.circularProgressBar.progress = percentage

        val color = when {
            percentage >= 70 -> ContextCompat.getColor(context, R.color.green)
            percentage >= 50 -> ContextCompat.getColor(context, R.color.yellow)
            else -> ContextCompat.getColor(context, R.color.red)
        }
        holder.circularProgressBar.progressBarColor = color
    }

    override fun getItemCount(): Int = list.size
}
