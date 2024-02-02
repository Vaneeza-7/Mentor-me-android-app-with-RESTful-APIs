package com.vaneezaahmad.i210390

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class Activity4 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_4)

        var back = findViewById<ImageView>(R.id.imageView1)
        back.setOnClickListener {
            startActivity(
                Intent(this, Activity3::class.java)
            );
        }
    }
}