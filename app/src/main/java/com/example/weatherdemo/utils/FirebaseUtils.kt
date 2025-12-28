package com.example.weatherdemo.utils

import android.util.Log
import com.example.weatherdemo.data.Message
import com.google.firebase.database.*

object FirebaseUtils {

    private val database = FirebaseDatabase.getInstance()

    // ✅ nodee khusus untuk chat global
    private val messagesRef = database.getReference("global_chat")

    fun sendMessage(username: String, message: String, time: String) {
        val id = messagesRef.push().key ?: return
        val msg = Message(
            id = id,
            username = username,
            message = message,
            time = time,
            timestamp = System.currentTimeMillis()
        )

        messagesRef.child(id).setValue(msg)
            .addOnFailureListener { e ->
                Log.e("FirebaseUtils", "sendMessage failed: ${e.message}", e)
            }
    }

    fun getMessages(callback: (List<Message>) -> Unit) {
        messagesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = mutableListOf<Message>()
                for (data in snapshot.children) {
                    val msg = data.getValue(Message::class.java)
                    if (msg != null) messages.add(msg)
                }

                // ✅ urutkan biar chat rapi
                messages.sortBy { it.timestamp }

                callback(messages)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseUtils", "getMessages cancelled: ${error.message}")
            }
        })
    }
}
