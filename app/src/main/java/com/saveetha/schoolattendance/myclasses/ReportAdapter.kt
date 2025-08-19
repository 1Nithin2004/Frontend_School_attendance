package com.saveetha.schoolattendance.myclasses
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.saveetha.schoolattendance.R
import com.saveetha.schoolattendance.service.response.ReportResponse

class ReportAdapter(private val reportList: List<ReportResponse>) :
    RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    inner class ReportViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sNoTV: TextView = view.findViewById(R.id.sNoTV)
        val subText: TextView = view.findViewById(R.id.subText)
        val rightText: TextView = view.findViewById(R.id.rightText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_report_layout, parent, false)
        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val student = reportList[position]
        holder.sNoTV.text = (position + 1).toString()
        holder.subText.text = student.name
        holder.rightText.text = "${student.attendance_percentage}%"

        // Color logic
        holder.rightText.setBackgroundColor(
            Color.parseColor(if (student.attendance_percentage < 60) "#F44336" else "#4CAF50")
        )
    }

    override fun getItemCount() = reportList.size
}
