package com.vaneezaahmad.i210390

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MessageActivity : AppCompatActivity() {
    val mAuth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        val back = findViewById<ImageButton>(R.id.back)
        val heading = findViewById<TextView>(R.id.heading)
        var mentorName = intent.getStringExtra("mentorName")
        val mentorEmail = intent.getStringExtra("mentorEmail")
        var mentorImage = intent.getStringExtra("mentorImage")

        heading.text = mentorName

        back.setOnClickListener {
                finish()
        }

        findViewById<ImageButton>(R.id.camera).setOnClickListener {
            val intent = Intent(this, Activity12::class.java)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.video).setOnClickListener {
            val intent = Intent(this, Activity14::class.java)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.call).setOnClickListener {
            val intent = Intent(this, Activity15::class.java)
            startActivity(intent)
        }

        val sendButton = findViewById<ImageButton>(R.id.send)
        val messageBox = findViewById<TextInputEditText>(R.id.message_box)

        var mentorUid : String = ""
        val mentorRef = database.getReference("Mentors")
        mentorRef.get().addOnSuccessListener {
            for (mentor in it.children) {
                val mentorObj = mentor.getValue(Mentor::class.java)
                if (mentorObj != null) {
                    if (mentorObj.email == mentorEmail) {
                        mentorUid = mentor.key.toString()
                        mentorImage = mentorObj.image

                    }
                }
            }
        }
        sendButton.setOnClickListener {
            val messageText = messageBox.text.toString()
            if (messageText.isNotEmpty()) {

                val sender = mAuth.currentUser?.uid
                val receiver = mentorUid
                val timestamp = System.currentTimeMillis()
                val read = false
                val receiverImage = mentorImage

                val message = sender?.let { it1 ->
                    Message(messageText,
                        it1, receiver, timestamp, read, receiverImage?:"")
                }

                val messageRef = database.getReference("messages")
                messageRef.push().setValue(message).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        messageBox.text?.clear()
                    }
                    else
                    {
                     Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }

        val recyclerView = findViewById<RecyclerView>(R.id.chat_recycler_view)
        val messages = mutableListOf<Message>()
        val messagesRef = database.getReference("messages")
        messagesRef.orderByChild("timestamp").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messages.clear()
                for (message in snapshot.children) {
                    val messageObj = message.getValue(Message::class.java)
                    if (messageObj != null) {
                        if (messageObj.sender == mAuth.currentUser?.uid && messageObj.receiver == mentorUid) {
                            messages.add(messageObj)
                        }
                        if (messageObj.sender == mentorUid && messageObj.receiver == mAuth.currentUser?.uid) {
                            messages.add(messageObj)
                        }
                    }
                }
                recyclerView.adapter = MessageAdapter(messages)
                recyclerView.adapter?.notifyDataSetChanged()
                recyclerView.scrollToPosition(messages.size - 1)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }
}
