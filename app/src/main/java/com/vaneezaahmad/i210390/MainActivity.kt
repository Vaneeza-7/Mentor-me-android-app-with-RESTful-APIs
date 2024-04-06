package com.vaneezaahmad.i210390

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.TextView
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONObject
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class MainActivity : AppCompatActivity() {
    val mAuth = FirebaseAuth.getInstance();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*        val value : ImageView = findViewById (R.id.logo)

        value.setBackgroundColor(resources.getColor(android.R.color.transparent))*/

        /*var btn = findViewById<TextView>(R.id.textView1)
        btn.setOnClickListener {
            startActivity(
                Intent(this, Activity2::class.java)
            );

        }*/

        if(mAuth.currentUser != null){
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, Activity7::class.java))
                finish()
            }, 5000) // 5000 milliseconds = 5 seconds
        }
        else
        {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, ActivityChoose::class.java))
                finish()
            }, 5000) // 5000 milliseconds = 5 seconds

        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.d(TAG, "FCM token: $token")
            //Toast.makeText(baseContext, "FCM Token: $token", Toast.LENGTH_SHORT).show()

            sendPushNotification(
                token,
                "Hello",
                "World",
                "This is a test notification",
                mapOf("key" to "value")
            )
        }
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
}