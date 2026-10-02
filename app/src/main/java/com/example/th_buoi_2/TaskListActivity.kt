package com.example.th_buoi_2

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity

// Màn 2: Quản lý công việc
class TaskListActivity : ComponentActivity() {

    private lateinit var listToday: LinearLayout
    private lateinit var listUpcoming: LinearLayout
    private lateinit var tvEmptyToday: TextView
    private lateinit var tvEmptyUpcoming: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks)

        listToday = findViewById(R.id.listToday)
        listUpcoming = findViewById(R.id.listUpcoming)
        tvEmptyToday = findViewById(R.id.tvEmptyToday)
        tvEmptyUpcoming = findViewById(R.id.tvEmptyUpcoming)

        findViewById<TextView>(R.id.tvUsername).text =
            intent.getStringExtra(MainActivity.EXTRA_USERNAME) ?: MainActivity.USERNAME

        findViewById<View>(R.id.btnAdd).setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }
    }

    // Load lại danh sách mỗi khi quay về từ màn thêm mới
    override fun onResume() {
        super.onResume()
        renderTasks()
    }

    private fun renderTasks() {
        renderSection(listToday, tvEmptyToday, TaskRepository.todayTasks())
        renderSection(listUpcoming, tvEmptyUpcoming, TaskRepository.upcomingTasks())
    }

    private fun renderSection(container: LinearLayout, emptyView: TextView, tasks: List<Task>) {
        container.removeAllViews()
        emptyView.visibility = if (tasks.isEmpty()) View.VISIBLE else View.GONE

        val inflater = LayoutInflater.from(this)
        for (task in tasks) {
            val card = inflater.inflate(R.layout.item_task, container, false)
            card.findViewById<TextView>(R.id.tvTaskName).text = task.name
            card.findViewById<TextView>(R.id.tvDeadline).text = "Hạn: ${task.deadlineText()}"

            val tvNote = card.findViewById<TextView>(R.id.tvNote)
            if (task.note.isNotBlank()) {
                tvNote.text = task.note
                tvNote.visibility = View.VISIBLE
            }

            // Tick hoàn thành -> ẩn công việc
            card.findViewById<CheckBox>(R.id.cbDone).setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    TaskRepository.remove(task.id)
                    card.animate().alpha(0f).setDuration(250).withEndAction {
                        renderTasks()
                    }.start()
                }
            }
            container.addView(card)
        }
    }
}
