package com.vaneezaahmad.i210390

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ActivityChoose : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose)

        val user = findViewById<TextView>(R.id.user)
        user.setOnClickListener {
            startActivity(
                Intent(this, Activity2::class.java)
            );
        }

        val mentor = findViewById<TextView>(R.id.mentorButton)
        mentor.setOnClickListener {
            startActivity(
                Intent(this, Activity10::class.java)
            );
        }
    }
}