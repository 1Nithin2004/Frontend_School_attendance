package com.saveetha.schoolattendance.myclasses

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.saveetha.schoolattendance.databinding.ActivityTimetableBinding

class TimetableActivity:AppCompatActivity() {

    lateinit var binding:ActivityTimetableBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimetableBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTimeTableRowAndColumn()

    }

    data class TimetableEntry(
        val period: Int,
        val day: String,
        val subject_name: String?,
        val teacher_name: String?
    )
    fun setTimeTableRowAndColumn() {
        val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")
        val periodMap = mapOf(
            1 to "1st", 2 to "2nd", 3 to "3rd", 4 to "4th", 5 to "5th", 6 to "6th", 7 to "7th", 8 to "8th"
        )

        val headerRow = TableRow(this)
        headerRow.addView(createTextView("Period", true))
        for (day in days) {
            headerRow.addView(createTextView(day, true))
        }
        binding.tableLayout.addView(headerRow)
        val timetableList = arrayOf(
            TimetableEntry(1, "Friday", "II Language", "Priya"),
            TimetableEntry(1, "Tuesday", "Maths", "John Mathew"),
            TimetableEntry(1, "Monday", "Social", "Mrs.Anjali"),
            TimetableEntry(1, "Wednesday", "PET", "Mr.Venakt"),
            TimetableEntry(1, "Thursday", "Computer Science", "Mr.Shasank"),

            TimetableEntry(2, "Wednesday", "English", "Sita Mehta"),
            TimetableEntry(2, "Monday", "II Language", "Priya"),
            TimetableEntry(2, "Friday", "Maths", "John Mathew"),
            TimetableEntry(2, "Thursday", "PET", "Mr.Venakt"),
            TimetableEntry(2, "Tuesday", "Computer Science", "Mr.Shasank"),

            TimetableEntry(3, "Monday", null, null),
            TimetableEntry(3, "Tuesday", null, null),
            TimetableEntry(3, "Wednesday", null, null),
            TimetableEntry(3, "Thursday", null, null),
            TimetableEntry(3, "Friday", null, null),

            TimetableEntry(4, "Wednesday", "English", "Sita Mehta"),
            TimetableEntry(4, "Monday", "II Language", "Priya"),
            TimetableEntry(4, "Friday", "Maths", "John Mathew"),
            TimetableEntry(4, "Thursday", "PET", "Mr.Venakt"),
            TimetableEntry(4, "Tuesday", "Computer Science", "Mr.Shasank"),

            TimetableEntry(5, "Monday", null, null),
            TimetableEntry(5, "Tuesday", null, null),
            TimetableEntry(5, "Wednesday", null, null),
            TimetableEntry(5, "Thursday", null, null),
            TimetableEntry(5, "Friday", null, null),

            TimetableEntry(6, "Wednesday", "English", "Sita Mehta"),
            TimetableEntry(6, "Monday", "II Language", "Priya"),
            TimetableEntry(6, "Friday", "Maths", "John Mathew"),
            TimetableEntry(6, "Thursday", "PET", "Mr.Venakt"),
            TimetableEntry(6, "Tuesday", "Computer Science", "Mr.Shasank"),

            TimetableEntry(7, "Monday", null, null),
            TimetableEntry(7, "Tuesday", null, null),
            TimetableEntry(7, "Wednesday", null, null),
            TimetableEntry(7, "Thursday", null, null),
            TimetableEntry(7, "Friday", null, null),

            TimetableEntry(8, "Wednesday", "English", "Sita Mehta"),
            TimetableEntry(8, "Monday", "II Language", "Priya"),
            TimetableEntry(8, "Friday", "Maths", "John Mathew"),
            TimetableEntry(8, "Thursday", "PET", "Mr.Venakt"),
            TimetableEntry(8, "Tuesday", "Computer Science", "Mr.Shasank")
        )

        val groupedByPeriod = timetableList.groupBy { it.period }

        for (period in 1..6) {
            val row = TableRow(this)
            row.addView(createTextView(periodMap[period] ?: "$period", false))
            for (day in days) {
                val entry = groupedByPeriod[period]?.find { it.day == day }
                val subject = entry?.subject_name ?: ""
                row.addView(createTextView(subject, false))
            }
            binding.tableLayout.addView(row)
        }

// Helper
    }

    fun createTextView(text: String, isHeader: Boolean): TextView {
        val tv = TextView(this)
        tv.text = text
        tv.setPadding(8, 8, 8, 8)
        tv.gravity = Gravity.CENTER
        tv.setTextColor(Color.BLACK)
        tv.setTypeface(null, if (isHeader) Typeface.BOLD else Typeface.NORMAL)
        return tv
    }

}