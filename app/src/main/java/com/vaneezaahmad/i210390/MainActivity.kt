package com.vaneezaahmad.i210390

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.TextView
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    val mAuth = FirebaseAuth.getInstance();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*        val value : ImageView = findViewById (R.id.logo)

        value.setBackgroundColor(resources.getColor(android.R.color.transparent))*/

        /*var btn = findViewById<TextView>(R.id.textView1)
        btn.setOnClickListener {
            startActivity(
                Intent(this, Activity2::class.java)
            );

        }*/

        if(mAuth.currentUser != null){
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, Activity7::class.java))
                finish()
            }, 5000) // 5000 milliseconds = 5 seconds
        }
        else
        {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, Activity2::class.java))
                finish()
            }, 5000) // 5000 milliseconds = 5 seconds

        }
    }
}