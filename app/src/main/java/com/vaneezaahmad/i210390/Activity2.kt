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

        }
    }
}