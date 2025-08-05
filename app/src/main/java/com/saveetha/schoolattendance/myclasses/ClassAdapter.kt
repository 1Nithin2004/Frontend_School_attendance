package com.saveetha.schoolattendance.myclasses

class ClassAdapter(
    private val classes: List<String>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<ClassAdapter.ClassViewHolder>() {

    inner class ClassViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val classText: TextView = view.findViewById(R.id.classText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_class, parent, false)
        return ClassViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val className = classes[position]
        holder.classText.text = className
        holder.itemView.setOnClickListener { onClick(className) }
    }

    override fun getItemCount() = classes.size
}
