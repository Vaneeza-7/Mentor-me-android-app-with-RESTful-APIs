package com.vaneezaahmad.i210390

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MentorLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentor_login)

        var email = findViewById<EditText>(R.id.email)
        var pass = findViewById<EditText>(R.id.password)
        var login = findViewById<TextView>(R.id.login)

        val signUp = findViewById<TextView>(R.id.signUp)
        signUp.setOnClickListener {
         finish();
        }

        login.setOnClickListener {
            val emailStr = email.text.toString()
            val passStr = pass.text.toString()
            if (emailStr.isNotEmpty() && passStr.isNotEmpty()) {
            }

        }

    }
}