package com.vaneezaahmad.i210390

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.Manifest
import android.annotation.SuppressLint
import android.media.MediaRecorder
import androidx.core.content.PackageManagerCompat.LOG_TAG
import java.io.IOException
import android.media.AudioAttributes
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
import io.agora.base.internal.voiceengine.WebRtcAudioRecord.setAudioSource
import java.io.File


class MessageActivity : AppCompatActivity() {
    val mAuth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance()
    private var recorder: MediaRecorder? = null
    private var fileName: String? = null
    companion object {
        const val REQUEST_RECORD_AUDIO_PERMISSION = 200
    }
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        val back = findViewById<ImageButton>(R.id.back)
        val heading = findViewById<TextView>(R.id.heading)
        val mic = findViewById<ImageButton>(R.id.mic)
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

        var isRecording = false

        mic.setOnClickListener {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_RECORD_AUDIO_PERMISSION)
                fileName = "${getExternalFilesDir(null)?.absolutePath}/audiorecordtest.3gp"
                 if (isRecording) {
                    Toast.makeText(this, "Recording stopped", Toast.LENGTH_SHORT).show()
                    stopRecording()
                     val file = File(fileName!!)
                    if(file.exists()) {
                        playRecordedAudio(fileName!!)
                        Log.d("Audio", "Audio file saved at $fileName")
                        val storageRef = FirebaseStorage.getInstance();
                        val st = storageRef.reference.child("voiceMessages/fileName")
                        val uploadTask = st.putFile(Uri.fromFile(file))
                        uploadTask.addOnSuccessListener {
                            Toast.makeText(this, "Audio file uploaded", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Toast.makeText(this, "Failed to upload audio file", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                     else
                    {
                        Toast.makeText(this, "Audio file not found", Toast.LENGTH_SHORT).show()
                    }

                    // Optionally, upload the audio file to your server or Firebase Storage here
                } else {
                    Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show()
                    startRecording(this@MessageActivity)
                }
                isRecording = !isRecording

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

                val messageRef = database.getReference("messages").push()
                val messageKey = messageRef.key

                val message = Message(messageText, sender, receiver, timestamp, read, receiverImage?:"", senderImage?:"", key = messageKey) // Set the key property of the Message object

                messageRef.setValue(message).addOnCompleteListener(this) { task ->
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
                        if (messageObj.sender == mAuth.currentUser?.uid.toString() && messageObj.receiver == userUid) {
                                messages.add(messageObj)
                        }
                        if (messageObj.sender == userUid && messageObj.receiver == mAuth.currentUser?.uid.toString()) {
                            messages.add(messageObj)
                        }
                    }
                }
                recyclerView.adapter = MessageAdapter(this@MessageActivity, messages)
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

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("RestrictedApi")
    private fun startRecording(context : Context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // If the RECORD_AUDIO permission has not been granted, request it
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_RECORD_AUDIO_PERMISSION)
        } else {
            // If the RECORD_AUDIO permission has been granted, initialize the MediaRecorder and start recording
            recorder = MediaRecorder(context).apply {
                try {
                    setAudioSource(MediaRecorder.AudioSource.MIC)
                    setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                    setOutputFile(fileName)
                    setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                    prepare()
                    start()
                } catch (e: Exception) {
                    // Handle the case where another app is using the microphone
                    Toast.makeText(context, "Microphone is currently in use by another app.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }

    @SuppressLint("RestrictedApi")
    private fun playRecordedAudio(audioFilePath: String) {
        val mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(audioFilePath)
                prepare()
                start()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "Could not play the audio", e)
            }
        }

        // Optional: Release the media player resources once the playback is complete
        mediaPlayer.setOnCompletionListener {
            it.release()
        }

        // Optional: Handle errors during playback
        mediaPlayer.setOnErrorListener { mp, what, extra ->
            Log.e(LOG_TAG, "Playback Error: $what, Extra Code: $extra")
            mp.release()
            true
        }
    }

}
