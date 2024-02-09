package com.vaneezaahmad.i210390

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

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

        val review = findViewById<Button>(R.id.review1)
        review.setOnClickListener {
            startActivity(
                Intent(this, Activity9::class.java)
            );
        }

        val book = findViewById<TextView>(R.id.bookSession)
        book.setOnClickListener {
            startActivity(
                Intent(this, Activity11::class.java)
            );
        }
    }
}