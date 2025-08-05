package com.saveetha.schoolattendance.myclasses

class ViewStudentsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val classList = listOf("Class 6", "Class 7", "Class 8", "Class 9", "Class 10")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_students)

        recyclerView = findViewById(R.id.classRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ClassAdapter(classList) { className ->
            val classNumber = className.split(" ")[1]
            val intent = Intent(this, StudentListActivity::class.java)
            intent.putExtra("classId", classNumber)
            startActivity(intent)
        }
    }
}
