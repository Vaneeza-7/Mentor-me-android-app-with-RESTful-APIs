package com.vaneezaahmad.i210390

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class Activity8 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_8)

        val back = findViewById<ImageView>(R.id.arrow)
        back.setOnClickListener {
            startActivity(
                Intent(this, Activity7::class.java)
            );
        }
    }
}