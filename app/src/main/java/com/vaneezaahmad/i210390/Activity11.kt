package com.vaneezaahmad.i210390

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class Activity11 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_11)

        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            startActivity(
                Intent(this, Activity8::class.java)
            );
        }
    }
}