package com.example.th_buoi_2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity

// Màn 1: Đăng nhập
class MainActivity : ComponentActivity() {

    companion object {
        const val USERNAME = "username"
        const val PASSWORD = "123456"
        const val EXTRA_USERNAME = "extra_username"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val edtUsername = findViewById<EditText>(R.id.edtUsername)
        val edtPassword = findViewById<EditText>(R.id.edtPassword)
        val tvError = findViewById<TextView>(R.id.tvError)

        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            val username = edtUsername.text.toString().trim()
            val password = edtPassword.text.toString()

            if (username == USERNAME && password == PASSWORD) {
                tvError.visibility = View.GONE
                val intent = Intent(this, TaskListActivity::class.java)
                intent.putExtra(EXTRA_USERNAME, username)
                startActivity(intent)
                finish()
            } else {
                tvError.text = "Sai tên đăng nhập hoặc mật khẩu"
                tvError.visibility = View.VISIBLE
            }
        }
    }
}
