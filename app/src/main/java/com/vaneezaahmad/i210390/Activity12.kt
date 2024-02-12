package com.vaneezaahmad.i210390

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class Activity12 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_12)

        val back = findViewById<ImageButton>(R.id.back)
        back.setOnClickListener {
           /* startActivity(
                Intent(this, Activity7::class.java)
            );*/
            finish();
        }

        val vid = findViewById<Button>(R.id.video)
        vid.setOnClickListener {
            startActivity(
                Intent(this, Activity13::class.java)
            );
        }

        val vid2 = findViewById<ImageButton>(R.id.imageButton2)
        vid2.setOnClickListener {
            startActivity(
                Intent(this, Activity13::class.java)
            );
        }
    }
}