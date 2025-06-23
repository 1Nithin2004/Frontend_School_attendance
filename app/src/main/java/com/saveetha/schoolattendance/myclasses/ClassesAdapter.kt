package com.saveetha.schoolattendance.myclasses

import android.location.GnssAntennaInfo.Listener
import com.saveetha.schoolattendance.databinding.RvClassListBinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.saveetha.schoolattendance.R

class ClassesAdapter(
    private val classList: List<MyClasses>,val Activity:FragmentActivity
) : RecyclerView.Adapter<ClassesAdapter.ClassViewHolder>() {

    var Listener:ClickListener?=null
    fun setupclicklistener(Listener:ClickListener)
    {
        this.Listener=Listener
    }
    interface ClickListener{
        fun click(data:MyClasses)
    }
    // ViewHolder class
    public class ClassViewHolder(val binding: RvClassListBinding) : RecyclerView.ViewHolder(binding.root) {
//        val classNameTextView: TextView = itemView.findViewById(R.id.textViewClassName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val view = RvClassListBinding.inflate(Activity.layoutInflater, parent, false)
        return ClassViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val schoolClass = classList[position]
        holder.binding.class1 .text = "Class ${schoolClass.class_name}"
        holder.binding.root.setOnClickListener{
            Listener?.click(schoolClass)
        }
    }

    override fun getItemCount(): Int = classList.size
}
