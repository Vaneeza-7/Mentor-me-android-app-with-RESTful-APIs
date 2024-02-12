package com.vaneezaahmad.i210390

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class Activity16 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_16)

        val back = findViewById<ImageButton>(R.id.arrow)
        back.setOnClickListener{
            finish()
        }

    }
}