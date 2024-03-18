package com.vaneezaahmad.i210390

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MentorLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentor_login)

        val signUp = findViewById<TextView>(R.id.signUp)
        signUp.setOnClickListener {
         finish();
        }

    }
}