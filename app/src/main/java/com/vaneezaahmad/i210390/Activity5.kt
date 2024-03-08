package com.vaneezaahmad.i210390

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Activity5 : AppCompatActivity() {
    var mAuth = FirebaseAuth.getInstance()
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
                mAuth.sendPasswordResetEmail(emailStr)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Email sent.",
                                Toast.LENGTH_SHORT).show()
                            startActivity(
                                Intent(this, Activity6::class.java)
                            );
                            finish();
                        } else {
                            Toast.makeText(this, "Failed to send email.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener(this) { exception ->
                        Toast.makeText(this, exception.localizedMessage,
                            Toast.LENGTH_LONG).show()
                    }
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