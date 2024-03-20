package com.vaneezaahmad.i210390

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessageAdapter(private var messages: List<Message>) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {
    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val message = view.findViewById<TextView>(R.id.text_message_body)
        val timestamp = view.findViewById<TextView>(R.id.text_message_time)
        val usermessage = view.findViewById<TextView>(R.id.user_message_body)
        val usertimestamp = view.findViewById<TextView>(R.id.user_message_time)
        val receiverImage = view.findViewById<CircleImageView>(R.id.profile_image)
        val userMessageLayout = view.findViewById<View>(R.id.user_message_layout)
        val mentorMessageLayout = view.findViewById<View>(R.id.mentor_message_layout)
    }
    var mAuth = FirebaseAuth.getInstance()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        val isCurrentUser = message.sender == mAuth.currentUser?.uid

        if (isCurrentUser) {
            // Align User's message to the right
            holder.userMessageLayout.visibility = View.VISIBLE
            holder.mentorMessageLayout.visibility = View.GONE
            holder.usermessage.text = message.message
            holder.usertimestamp.text = convertTimestampToTime(message.timestamp)
        } else {
            // Align Mentor's message to the left
            holder.userMessageLayout.visibility = View.GONE
            holder.mentorMessageLayout.visibility = View.VISIBLE
            holder.message.text = message.message
            holder.timestamp.text = convertTimestampToTime(message.timestamp)
            Glide.with(holder.receiverImage.context).load(message.receiverImage).into(holder.receiverImage)
        }
            //TODO Change colour of message bubble
    }

    override fun getItemCount() = messages.size


    fun convertTimestampToTime(timestamp: Long): String {
        val date = Date(timestamp)
        val format = SimpleDateFormat("hh:mm a", Locale.getDefault()) // For example, '02:30 PM'
        return format.format(date)
    }

}