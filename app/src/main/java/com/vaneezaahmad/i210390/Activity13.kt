package com.vaneezaahmad.i210390

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class Activity13 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_13)

        val back = findViewById<ImageButton>(R.id.back)
        back.setOnClickListener {
            startActivity(
                Intent(this, Activity7::class.java)
            );
        }

        val pic = findViewById<Button>(R.id.photo)
        pic.setOnClickListener {
            startActivity(
                Intent(this, Activity12::class.java)
            );
        }

        val pic2 = findViewById<ImageButton>(R.id.imageButton2)
        pic2.setOnClickListener {
            startActivity(
                Intent(this, Activity12::class.java)
            );
        }

    }
}