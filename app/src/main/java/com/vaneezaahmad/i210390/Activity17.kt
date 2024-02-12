package com.vaneezaahmad.i210390

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class Activity17 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_17)

        val back = findViewById<ImageButton>(R.id.backButton)
        back.setOnClickListener{

            finish();
        }
    }
}