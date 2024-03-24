package com.vaneezaahmad.i210390

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.vaneezaahmad.i210390.AgoraManager
class Activity14 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_14)
        val agoraManager = AgoraManager()

        val close = findViewById<ImageButton>(R.id.closeButton)
        close.setOnClickListener{

            /*startActivity(
                Intent(this, Activity7::class.java)
            ) ;*/
            finish();
        }

        agoraManager.initAgoraEngine(this, agoraManager.appId)


    }
}