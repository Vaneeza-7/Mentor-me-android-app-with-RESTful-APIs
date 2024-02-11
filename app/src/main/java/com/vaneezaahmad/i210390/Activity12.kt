package com.vaneezaahmad.i210390

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class Activity12 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_12)

        val back = findViewById<ImageButton>(R.id.back)
        back.setOnClickListener {
            startActivity(
                Intent(this, Activity7::class.java)
            );
        }
    }
}