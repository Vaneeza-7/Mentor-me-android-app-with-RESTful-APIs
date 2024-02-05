package com.vaneezaahmad.i210390

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.content.Intent
import android.view.View

class Activity6 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_6)


        var back = findViewById<ImageView>(R.id.arrow)
        back.setOnClickListener {
            startActivity(
                Intent(this, Activity5::class.java)
            );
        }

        var login = findViewById<View>(R.id.login)
        login.setOnClickListener {
            startActivity(
                Intent(this, Activity2::class.java)
            );
        }

        var home = findViewById<View>(R.id.submit)
        home.setOnClickListener {
            startActivity(
                Intent(this, Activity7::class.java)
            );
        }
    }

}