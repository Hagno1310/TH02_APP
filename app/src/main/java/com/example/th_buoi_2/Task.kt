package com.example.th_buoi_2

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class Task(
    val id: Long,
    val name: String,
    val deadline: Long, // epoch millis
    val note: String
) {
    fun deadlineText(): String =
        SimpleDateFormat("HH:mm - dd/MM/yyyy", Locale.getDefault()).format(Date(deadline))
}

// Lưu danh sách công việc trong bộ nhớ (mất khi tắt app)
object TaskRepository {

    private var nextId = 1L
    private val tasks = mutableListOf<Task>()

    init {
        val now = Calendar.getInstance()
        add("Họp nhóm dự án", atHour(now, 0, 15, 0), "Phòng họp tầng 3")
        add("Nộp bài thực hành buổi 2", atHour(now, 0, 21, 0), "")
        add("Đi siêu thị", atHour(now, 1, 18, 30), "Mua rau, trứng, sữa")
        add("Ôn thi giữa kỳ", atHour(now, 3, 9, 0), "Chương 1 - 4")
    }

    fun add(name: String, deadline: Long, note: String) {
        tasks.add(Task(nextId++, name, deadline, note))
    }

    fun remove(id: Long) {
        tasks.removeAll { it.id == id }
    }

    // Công việc có hạn từ hết hôm nay trở về trước
    fun todayTasks(): List<Task> =
        tasks.filter { it.deadline <= endOfToday() }.sortedBy { it.deadline }

    // Công việc có hạn từ ngày mai trở đi
    fun upcomingTasks(): List<Task> =
        tasks.filter { it.deadline > endOfToday() }.sortedBy { it.deadline }

    private fun endOfToday(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        return cal.timeInMillis
    }

    private fun atHour(base: Calendar, plusDays: Int, hour: Int, minute: Int): Long {
        val cal = base.clone() as Calendar
        cal.add(Calendar.DAY_OF_YEAR, plusDays)
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }
}
