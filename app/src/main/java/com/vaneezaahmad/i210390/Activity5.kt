package com.vaneezaahmad.i210390

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class Activity5 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_5)

        var back = findViewById<ImageView>(R.id.arrow)
        back.setOnClickListener {
            startActivity(
                Intent(this, Activity2::class.java)
            );
        }
    }
}