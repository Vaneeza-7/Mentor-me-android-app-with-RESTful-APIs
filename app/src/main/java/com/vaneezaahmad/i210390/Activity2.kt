package com.vaneezaahmad.i210390

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class Activity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_2)

        var btn = findViewById<TextView>(R.id.signUp)
        btn.setOnClickListener {
            startActivity(
                Intent(this, Activity3::class.java)
            );

            //implicit intent

        }

        var btn2 = findViewById<TextView>(R.id.forgotPassword)
        btn2.setOnClickListener {
            startActivity(
                Intent(this, Activity5::class.java)
            );
        }

        var login = findViewById<TextView>(R.id.login)
        login.setOnClickListener {
            startActivity(
                Intent(this, Activity7::class.java)
            );
        }


    }
}