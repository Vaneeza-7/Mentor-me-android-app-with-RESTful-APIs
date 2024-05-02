package com.vaneezaahmad.i210390

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.content.Intent
import android.view.View
import android.widget.EditText
import android.widget.Toast

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

        var resetPass = findViewById<View>(R.id.submit)
        /*resetPass.setOnClickListener {
            startActivity(
                Intent(this, Activity7::class.java)
            );
        }*/
        resetPass.setOnClickListener {
            val newPassword = findViewById<EditText>(R.id.newPassword).text.toString()
            val reEnterPassword = findViewById<EditText>(R.id.reEnterPassword).text.toString()

            if (newPassword == reEnterPassword) {
            }
        }
    }

}