package com.vaneezaahmad.i210390

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText

import com.google.firebase.messaging.FirebaseMessaging
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID


class MessageActivity : ScreenshotDetectionActivity() {
    private var recorder: MediaRecorder? = null
    private var fileName: String? = null
    private var photoUri: Uri? = null
    private var currentPhotoPath: String? = null


    companion object {
        const val REQUEST_RECORD_AUDIO_PERMISSION = 200
        private const val REQUEST_CODE_READ_EXTERNAL_STORAGE = 100
        private const val REQUEST_CODE_CAMERA = 101
    }

    /*override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_READ_EXTERNAL_STORAGE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission was granted
                    // You can start the screenshot detection here
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    // Permission was denied
                    // Show a message to the user explaining why the permission is needed
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
            // Handle other permission results
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        }
    }*/

    override fun onScreenCaptured(path: String) {
        super.onScreenCaptured(path)
        Toast.makeText(this, "Screenshot captured: $path", Toast.LENGTH_SHORT).show()
    }

    override fun onScreenCapturedWithDeniedPermission() {
        super.onScreenCapturedWithDeniedPermission()
        //Toast.makeText(this, "Please grant permission", Toast.LENGTH_SHORT).show()
        Toast.makeText(this, "Screenshot Captured", Toast.LENGTH_SHORT).show()
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.d(ContentValues.TAG, "FCM token: $token")
            Toast.makeText(baseContext, "FCM Token: $token", Toast.LENGTH_SHORT).show()

            sendPushNotification(
                token,
                "Screenshot Captured",
                "Screenshot",
                "A screenshot has been captured",
                mapOf("key" to "value")
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // If not, request the permission
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_READ_EXTERNAL_STORAGE)
        }

        val back = findViewById<ImageButton>(R.id.back)
        val heading = findViewById<TextView>(R.id.heading)
        val mic = findViewById<ImageButton>(R.id.mic)
        val editText = findViewById<TextInputEditText>(R.id.message_box)
        var mentorName = intent.getStringExtra("mentorName")
        val mentorEmail = intent.getStringExtra("mentorEmail")
        var mentorImage = intent.getStringExtra("mentorImage")
        var userName = intent.getStringExtra("userName")
        var userEmail = intent.getStringExtra("userEmail")
        var userImage = intent.getStringExtra("userImage")
        var receiverImage: String ? = ""
        var senderImage : String ? = ""
        var audioUrl = ""
        var mediaUrl = ""

        if(mentorName != null) {
            heading.text = mentorName
        }
        else if (userName != null) {
            heading.text = userName
        }

        back.setOnClickListener {
                finish()
        }


        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val uri = data?.data
                val mimeType = contentResolver.getType(uri!!) // Get the MIME type
                val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
                // Save the uri to firebase storage

            }
        }

        val gallery = findViewById<ImageButton>(R.id.gallery)
        gallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("*/*")
            val mimeTypes = arrayOf("image/*", "video/*")
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            resultLauncher.launch(intent)

        }

        var mentorUid = intent.getStringExtra("mentorUid")

        var userUid : String = ""

        val recyclerView = findViewById<RecyclerView>(R.id.chat_recycler_view)
        val messages = mutableListOf<Message>()

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
                        //playRecordedAudio(fileName!!)
                        Log.d("Audio", "Audio file saved at $fileName")
                        //val storageRef = FirebaseStorage.getInstance();
                        //val st = storageRef.reference.child("voiceMessages/${mAuth.currentUser?.uid}/${System.currentTimeMillis()}.3gp")
                       // val uploadTask = st.putFile(Uri.fromFile(file))
//                        uploadTask.addOnSuccessListener {
//                            Toast.makeText(this, "Press Send", Toast.LENGTH_SHORT).show()
//                            st.downloadUrl.addOnSuccessListener {
//                                    audioUrl = it.toString()
//                            }
//                        }.addOnFailureListener {
//                            Toast.makeText(this, "Failed to upload audio file", Toast.LENGTH_SHORT)
//                                .show()
//                        }
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

        val resultLauncherCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                //val uri = data?.data
                val mimeType = contentResolver.getType(photoUri!!) // Get the MIME type
                val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
                // Save the uri to firebase storage
                //val storageRef = FirebaseStorage.getInstance();
                //val st = storageRef.reference.child("chatMedia/${mAuth.currentUser?.uid}/${System.currentTimeMillis()}.$extension")
                //val uploadTask = st.putFile(photoUri!!)
//                uploadTask.addOnSuccessListener {
//                    Toast.makeText(this, "Media uploaded successfully", Toast.LENGTH_SHORT).show()
//                    st.downloadUrl.addOnSuccessListener {
//                        mediaUrl = it.toString()
//                        Toast.makeText(this, "Press Send", Toast.LENGTH_SHORT).show()
//                    }
//                }.addOnFailureListener {
//                    Toast.makeText(this, "Failed to upload media", Toast.LENGTH_SHORT).show()
//                }

            }
        }

        findViewById<ImageButton>(R.id.camera).setOnClickListener {
          /*  val intent = Intent(this, Activity12::class.java)
            if(mentorName != null) {
                intent.putExtra("mentorName", mentorName)
                intent.putExtra("mentorImage", mentorImage)
            }
            else {
                intent.putExtra("userName", userName)
                intent.putExtra("userImage", userImage)
            }
            //startActivity(intent);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_CAMERA)
            } else {*/

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_CAMERA)
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.vaneezaahmad.i210390.fileprovider",
                        it
                    )
                     photoUri = FileProvider.getUriForFile(
                        this,
                        "com.vaneezaahmad.i210390.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    resultLauncherCamera.launch(takePictureIntent)
                }
            }

            //}
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

//        var mentorUid : String = ""
//        val mentorRef = database.getReference("Mentors")
//        mentorRef.get().addOnSuccessListener {
//            for (mentor in it.children) {
//                val mentorObj = mentor.getValue(Mentor::class.java)
//                if (mentorObj != null) {
//                    if (mentorObj.email == mentorEmail) {
//                        mentorUid = mentor.key.toString()
//                        mentorImage = mentorObj.image
//
//                    }
//                }
//            }
//        }


//getting current user's profile image


        sendButton.setOnClickListener {
            val messageText = messageBox.text.toString()
            //if (messageText.isNotEmpty()) {

                var message : Message;
                val sender = "vaneezay"
                val receiver : String;
                val timestamp = System.currentTimeMillis()
                val read = false
                val messageKey = UUID.randomUUID().toString()

                if (userEmail != null) {
                    // If userEmail is not null, then the sender is the mentor and the receiver is the user
                    receiver = userUid
                    receiverImage = userImage
                } else {
                    // If userEmail is null, then the sender is the user and the receiver is the mentor
                    receiver = mentorUid!!
                    receiverImage = mentorImage
                }

                if(messageText.isNotEmpty() && audioUrl.isEmpty()){
                    val type = "text"
                    message = Message(messageText, sender, receiver, timestamp, read, receiverImage?:"", senderImage?:"", key = messageKey, audioUrl = "", mediaUrl = "", type = type)
                }
                else if (mediaUrl.isNotEmpty()) {
                    val type = "media"
                    message = Message("Media Message", sender, receiver, timestamp, read, receiverImage?:"", senderImage?:"", key = messageKey, audioUrl = "", mediaUrl = mediaUrl, type = type)
                }
                else {
                    val type = "audio"
                    message = Message("Voice Message", sender, receiver, timestamp, read, receiverImage?:"", senderImage?:"", key = messageKey, audioUrl = audioUrl, mediaUrl = "", type = type)
                }
//                messageRef.setValue(message).addOnCompleteListener(this) { task ->
//                    if (task.isSuccessful) {
//                        messageBox.text?.clear()
//                    }
//                    else
//                    {
//                        Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show()
//                    }
//                }

            //}
        }

    }

    fun fetchCurrentUserImage(currentUserId: String, onComplete: (String?) -> Unit) {
        // find the image in the Mentors reference
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

    fun sendPushNotification(token: String, title: String, subtitle: String, body: String, data: Map<String, String> = emptyMap()) {
        val url = "https://fcm.googleapis.com/fcm/send"
        val bodyJson = JSONObject()
        bodyJson.put("to", token)
        bodyJson.put("notification",
            JSONObject().also {
                it.put("title", title)
                it.put("subtitle", subtitle)
                it.put("body", body)
                it.put("sound", "social_notification_sound.wav")
            }
        )
        if (data.isNotEmpty()) {
            bodyJson.put("data", JSONObject(data))
        }
        var key="AAAAMbKQQnE:APA91bEw8UMuubt6jFAA69kX3przxhr7n3hqcsyq089ajTW-ZpHS5ZFJQ1zlNgPGJXJhiWt5eiXLID6IsZS5Tc37PQVwvNb9G9vB1ZAlning83utDYaLYHpBNmB3aKqZJxkbmpdx3YIU"
        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "key=$key")
            .post(
                bodyJson.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
            )
            .build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(
            object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    Log.d("TAG", "onResponse: ${response} ")
                }
                override fun onFailure(call: Call, e: IOException) {Log.d("TAG", "onFailure: ${e.message.toString()}")
                } } )
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

}
