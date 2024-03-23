package com.vaneezaahmad.i210390

import android.app.AlertDialog
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessageAdapter(private val context: Context, private var messages: MutableList<Message>) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {
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
    var database = FirebaseDatabase.getInstance()
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
            holder.userMessageLayout.setOnLongClickListener {
                showDialogWithOptions(position)
                true
            }
            holder.mentorMessageLayout.setOnLongClickListener {
                showDialogWithOptions(position)
                true
            }
        } else {
            // Align Mentor's message to the left
            holder.userMessageLayout.visibility = View.GONE
            holder.mentorMessageLayout.visibility = View.VISIBLE
            holder.message.text = message.message
            holder.timestamp.text = convertTimestampToTime(message.timestamp)
            Glide.with(holder.receiverImage.context).load(message.senderImage).into(holder.receiverImage)
            holder.message.setTextColor(ContextCompat.getColor(holder.message.context, R.color.black))
            val backgroundColor = ContextCompat.getColor(holder.mentorMessageLayout.context, R.color.white)
            ViewCompat.setBackgroundTintList(holder.mentorMessageLayout, ColorStateList.valueOf(backgroundColor))
            holder.message.background = ContextCompat.getDrawable(holder.mentorMessageLayout.context, R.color.white)
        }

    }

    override fun getItemCount() = messages.size


    fun convertTimestampToTime(timestamp: Long): String {
        val date = Date(timestamp)
        val format = SimpleDateFormat("hh:mm a", Locale.getDefault()) // For example, '02:30 PM'
        return format.format(date)
    }

    private fun showDialogWithOptions(position: Int) {
        val options = arrayOf("Edit", "Delete")
        val builder = AlertDialog.Builder(context)
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> { // Edit
                    // Call your method to show dialog for editing message
                    showDialogToEditMessage(position)
                }
                1 -> { // Delete
                    // Call your method to delete message
                    deleteMessage(position)
                }
            }
        }
        builder.show()
    }

    private fun showDialogToEditMessage(position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Edit Message")

        val input = EditText(context)
        input.setText(messages[position].message)
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            val newMessage = input.text.toString()
            messages[position].message = newMessage
            messages[position].timestamp = System.currentTimeMillis()
            messages[position].read = false
            messages[position].receiverImage = messages[position].receiverImage
            messages[position].senderImage = messages[position].senderImage
            notifyItemChanged(position)
            dialog.dismiss()

            // Update the message in Firebase Realtime Database
            val messageKey = messages[position].key // Assuming 'key' is a property in your 'Message' model
            database.getReference("messages").child(messageKey!!).setValue(messages[position])
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun deleteMessage(position: Int) {
        // Delete the message from Firebase Realtime Database
        val messageKey = messages[position].key // Assuming 'key' is a property in your 'Message' model
        database.getReference("messages").child(messageKey!!).removeValue()

        // Remove the message from your list and notify the adapter
        messages.removeAt(position)
        notifyItemRemoved(position)
    }
}