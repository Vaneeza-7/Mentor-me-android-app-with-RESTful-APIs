package com.vaneezaahmad.i210390

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.res.ColorStateList
import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView
import java.io.IOException
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
        val receiverImage2 = view.findViewById<ImageView>(R.id.profile_image2)
        val userMessageLayout = view.findViewById<View>(R.id.user_message_layout)
        val mentorMessageLayout = view.findViewById<View>(R.id.mentor_message_layout)
        val userVoiceMessageLayout = view.findViewById<View>(R.id.user_voice_message_layout)
        val mentorVoiceMessageLayout = view.findViewById<View>(R.id.mentor_voice_message_layout)
        val user_voice_message_time = view.findViewById<TextView>(R.id.user_voice_message_time)
        val mentor_voice_message_time = view.findViewById<TextView>(R.id.mentor_voice_message_time)
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

        when (message.type) {
            "text" -> {
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
                    Glide.with(holder.receiverImage.context).load(message.senderImage)
                        .into(holder.receiverImage)
                    holder.message.setTextColor(
                        ContextCompat.getColor(
                            holder.message.context,
                            R.color.black
                        )
                    )
                    val backgroundColor =
                        ContextCompat.getColor(holder.mentorMessageLayout.context, R.color.white)
                    ViewCompat.setBackgroundTintList(
                        holder.mentorMessageLayout,
                        ColorStateList.valueOf(backgroundColor)
                    )
                    holder.message.background =
                        ContextCompat.getDrawable(holder.mentorMessageLayout.context, R.color.white)
                }
            }

            "audio" -> {
                // Handle audio messages
                if (isCurrentUser) {
                    holder.userVoiceMessageLayout.visibility = View.VISIBLE
                    holder.mentorVoiceMessageLayout.visibility = View.GONE
                    holder.user_voice_message_time.text = convertTimestampToTime(message.timestamp)
                    holder.userVoiceMessageLayout.setOnLongClickListener {
                        deleteMessage(position)
                        true
                    }
                    holder.mentorVoiceMessageLayout.setOnLongClickListener {
                        deleteMessage(position)
                        true
                    }
                    holder.userVoiceMessageLayout.setOnClickListener {
                        // Play the audio message
                        Toast.makeText(context, "Play audio message", Toast.LENGTH_SHORT).show()
                        playRecordedAudio(message.audioUrl!!)
                    }
                } else {
                    holder.userVoiceMessageLayout.visibility = View.GONE
                    holder.mentorVoiceMessageLayout.visibility = View.VISIBLE
                    holder.mentor_voice_message_time.text = convertTimestampToTime(message.timestamp)
                    Glide.with(holder.receiverImage2.context).load(message.senderImage)
                        .into(holder.receiverImage2)
                    holder.message.setTextColor(
                        ContextCompat.getColor(
                            holder.message.context,
                            R.color.black
                        )
                    )
                    val backgroundColor =
                        ContextCompat.getColor(holder.mentorVoiceMessageLayout.context, R.color.white)
                    ViewCompat.setBackgroundTintList(
                        holder.mentorVoiceMessageLayout,
                        ColorStateList.valueOf(backgroundColor)
                    )
                    holder.message.background =
                        ContextCompat.getDrawable(holder.mentorVoiceMessageLayout.context, R.color.white)
                    holder.mentorVoiceMessageLayout.setOnClickListener {
                        // Play the audio message
                        Toast.makeText(context, "Play audio message", Toast.LENGTH_SHORT).show()
                        playRecordedAudio(message.audioUrl!!)
                    }
                }
            }
        }

    }

    override fun getItemCount() = messages.size


    fun convertTimestampToTime(timestamp: Long): String {
        val date = Date(timestamp)
        val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return format.format(date)
    }

    private fun showDialogWithOptions(position: Int) {
        val options = arrayOf("Edit", "Delete")
        val builder = AlertDialog.Builder(context)
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> { // Edit
                    showDialogToEditMessage(position)
                }
                1 -> { // Delete
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


            val messageKey = messages[position].key
            database.getReference("messages").child(messageKey!!).setValue(messages[position])
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun deleteMessage(position: Int) {

        val messageKey = messages[position].key
        database.getReference("messages").child(messageKey!!).removeValue()


        messages.removeAt(position)
        notifyItemRemoved(position)
    }

    @SuppressLint("RestrictedApi")
    private fun playRecordedAudio(audioFilePath: String) {
        val mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(audioFilePath)
                prepare()
                start()
            } catch (e: IOException) {
                Log.e(PackageManagerCompat.LOG_TAG, "Could not play the audio", e)
            }
        }

        // Optional: Release the media player resources once the playback is complete
        mediaPlayer.setOnCompletionListener {
            it.release()
        }

        // Optional: Handle errors during playback
        mediaPlayer.setOnErrorListener { mp, what, extra ->
            Log.e(PackageManagerCompat.LOG_TAG, "Playback Error: $what, Extra Code: $extra")
            mp.release()
            true
        }
    }
}