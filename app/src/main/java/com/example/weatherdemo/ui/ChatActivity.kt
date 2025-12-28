package com.example.weatherdemo.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherdemo.R
import com.example.weatherdemo.ui.adapter.ChatAdapter
import com.example.weatherdemo.utils.FirebaseUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatActivity : AppCompatActivity() {

    private lateinit var etMessage: EditText
    private lateinit var btnSend: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var username: String   // username user aktif

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // Toolbar + tombol back
        val toolbar = findViewById<Toolbar>(R.id.toolbarChat)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        // ✅ Ambil username dari Intent
        username = intent.getStringExtra("username") ?: "Guest"

        etMessage = findViewById(R.id.etMessage)
        btnSend = findViewById(R.id.btnSend)
        recyclerView = findViewById(R.id.recyclerView)

        val layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }
        recyclerView.layoutManager = layoutManager

        // 🔥 WAJIB kirim username ke ChatAdapter
        chatAdapter = ChatAdapter(username)
        recyclerView.adapter = chatAdapter

        btnSend.setOnClickListener {
            val messageText = etMessage.text.toString().trim()

            if (messageText.isNotEmpty()) {

                val time = SimpleDateFormat(
                    "HH:mm",
                    Locale.getDefault()
                ).format(Date())

                FirebaseUtils.sendMessage(
                    username = username,
                    message = messageText,
                    time = time
                )

                etMessage.setText("")
            }
        }
    }

    override fun onStart() {
        super.onStart()

        FirebaseUtils.getMessages { messages ->
            chatAdapter.submitList(messages)

            if (messages.isNotEmpty()) {
                recyclerView.post {
                    recyclerView.scrollToPosition(messages.size - 1)
                }
            }
        }
    }
}
