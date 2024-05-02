package com.vaneezaahmad.i210390

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast


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

        var next = findViewById<View>(R.id.submit)
        /*next.setOnClickListener {
            startActivity(
                Intent(this, Activity6::class.java)
            );
        }*/
        next.setOnClickListener {
            val emailStr = findViewById<TextView>(R.id.email).text.toString()
            if (emailStr.isNotEmpty()) {
            }
        }

        var login = findViewById<View>(R.id.login)
        login.setOnClickListener {
            startActivity(
                Intent(this, Activity2::class.java)
            );
        }

    }
}