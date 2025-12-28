package com.example.weatherdemo.ui.adapter

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherdemo.R
import com.example.weatherdemo.data.Message

class ChatAdapter(
    private val currentUsername: String
) : RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    private val messageList = mutableListOf<Message>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messageList[position])
    }

    override fun getItemCount(): Int = messageList.size

    fun submitList(messages: List<Message>) {
        messageList.clear()
        messageList.addAll(messages)
        notifyDataSetChanged()
    }

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val container: LinearLayout =
            itemView.findViewById(R.id.container)

        private val cardMessage: CardView =
            itemView.findViewById(R.id.cardMessage)

        private val tvUsername: TextView =
            itemView.findViewById(R.id.tvUsername)

        private val tvMessage: TextView =
            itemView.findViewById(R.id.tvMessage)

        private val tvTime: TextView =
            itemView.findViewById(R.id.tvTime)

        fun bind(message: Message) {
            tvUsername.text = message.username
            tvMessage.text = message.message
            tvTime.text = message.time

            val params = container.layoutParams as RecyclerView.LayoutParams

            if (message.username == currentUsername) {
                // ================= USER SENDIRI (KANAN) =================
                container.gravity = Gravity.END
                params.marginStart = 100
                params.marginEnd = 0

                cardMessage.setCardBackgroundColor(
                    Color.parseColor("#DCF8C6")
                )

                tvUsername.visibility = View.VISIBLE   // ✅ FIX UTAMA

            } else {
                // ================= USER LAIN (KIRI) =================
                container.gravity = Gravity.START
                params.marginStart = 0
                params.marginEnd = 100

                cardMessage.setCardBackgroundColor(
                    Color.parseColor("#E3F2FD")
                )

                tvUsername.visibility = View.VISIBLE
            }

            container.layoutParams = params
        }
    }
}
