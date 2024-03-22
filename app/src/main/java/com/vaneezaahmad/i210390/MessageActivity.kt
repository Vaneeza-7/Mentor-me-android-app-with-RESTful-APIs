package com.vaneezaahmad.i210390

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
        var userName = intent.getStringExtra("userName")
        var userEmail = intent.getStringExtra("userEmail")
        var userImage = intent.getStringExtra("userImage")
        var receiverImage: String ? = ""
        var senderImage : String ? = ""


        if(mentorName != null) {
            heading.text = mentorName
        }
        else if (userName != null) {
            heading.text = userName
        }

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

        val userRef = database.getReference("users")
        var userUid : String = ""
        userRef.get().addOnSuccessListener {
            for (user in it.children) {
                val userObj = user.getValue(User::class.java)
                if (userObj != null) {
                    if (userObj.email == userEmail) {
                        userUid = user.key.toString()
                        userImage = userObj.image
                    }
                }
            }
        }

        val currentUserId = mAuth.currentUser?.uid
        if (currentUserId != null) {
            fetchCurrentUserImage(currentUserId) { imageUrl ->
                if (imageUrl != null) {
                    senderImage = imageUrl
                } else {
                    // Handle case where image is not found
                }
            }
        }

        sendButton.setOnClickListener {
            val messageText = messageBox.text.toString()
            if (messageText.isNotEmpty()) {

                val sender = mAuth.currentUser?.uid.toString()
                val receiver : String;
                val timestamp = System.currentTimeMillis()
                val read = false

                if (userEmail != null) {
                    // If userEmail is not null, then the sender is the mentor and the receiver is the user
                    receiver = userUid
                    receiverImage = userImage
                } else {
                    // If userEmail is null, then the sender is the user and the receiver is the mentor
                    receiver = mentorUid
                    receiverImage = mentorImage
                }

                Log.d("MessageActivity", "receiverImage: $receiverImage") // Add this line
                Log.d("MessageActivity", "senderImage: $senderImage") // Add this line

                val message = Message(messageText, sender, receiver, timestamp, read, receiverImage?:"", senderImage?:"")

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
                        if (messageObj.sender == mAuth.currentUser?.uid.toString() || messageObj.receiver == mAuth.currentUser?.uid.toString()) {
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

    fun fetchCurrentUserImage(currentUserId: String, onComplete: (String?) -> Unit) {
        // find the image in the Mentors reference
        val mentorRef = database.getReference("Mentors").child(currentUserId)
        mentorRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val mentor = dataSnapshot.getValue(Mentor::class.java)
                if (mentor != null && mentor.image.isNotEmpty()) {
                    onComplete(mentor.image)
                } else {
                    // If not found in Mentors, then look in the Users reference
                    val userRef = database.getReference("users").child(currentUserId)
                    userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val user = snapshot.getValue(User::class.java)
                            if (user != null && user.image.isNotEmpty()) {
                                onComplete(user.image)
                            } else {
                                // User image also not found, handle accordingly
                                onComplete(null)
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Handle error case
                            onComplete(null)
                        }
                    })
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error case
                onComplete(null)
            }
        })
    }
}
