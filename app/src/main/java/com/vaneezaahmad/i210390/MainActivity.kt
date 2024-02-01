package com.vaneezaahmad.i210390

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*        val value : ImageView = findViewById (R.id.logo)

        value.setBackgroundColor(resources.getColor(android.R.color.transparent))*/

        var btn = findViewById<TextView>(R.id.textView1)
        btn.setOnClickListener {
            startActivity(
                Intent(this, Activity2::class.java)
            );

        }
    }
}