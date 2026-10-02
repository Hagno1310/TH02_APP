package com.example.th_buoi_2

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddTaskActivity : ComponentActivity() {

    private var deadline: Calendar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        val edtTaskName = findViewById<EditText>(R.id.edtTaskName)
        val edtDeadline = findViewById<EditText>(R.id.edtDeadline)
        val edtNote = findViewById<EditText>(R.id.edtNote)
        val tvError = findViewById<TextView>(R.id.tvError)

        edtDeadline.setOnClickListener { pickDeadline(edtDeadline) }

        findViewById<Button>(R.id.btnSave).setOnClickListener {
            val name = edtTaskName.text.toString().trim()
            val selected = deadline
            when {
                name.isEmpty() -> showError(tvError, "Vui lòng nhập tên công việc")
                selected == null -> showError(tvError, "Vui lòng chọn thời gian hoàn thành")
                else -> {
                    TaskRepository.add(name, selected.timeInMillis, edtNote.text.toString().trim())
                    finish()
                }
            }
        }

        findViewById<Button>(R.id.btnCancel).setOnClickListener { finish() }
    }

    private fun pickDeadline(target: EditText) {
        val cal = deadline ?: Calendar.getInstance()
        DatePickerDialog(this, { _, year, month, day ->
            TimePickerDialog(this, { _, hour, minute ->
                val picked = Calendar.getInstance()
                picked.set(year, month, day, hour, minute, 0)
                picked.set(Calendar.MILLISECOND, 0)
                deadline = picked
                target.setText(
                    SimpleDateFormat("HH:mm - dd/MM/yyyy", Locale.getDefault()).format(picked.time)
                )
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun showError(tvError: TextView, message: String) {
        tvError.text = message
        tvError.visibility = View.VISIBLE
    }
}
