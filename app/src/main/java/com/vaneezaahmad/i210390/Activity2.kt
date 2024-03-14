package com.vaneezaahmad.i210390

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Activity2 : AppCompatActivity() {
    var mAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_2)

        var btn = findViewById<TextView>(R.id.signUp)
        btn.setOnClickListener {
            startActivity(
                Intent(this, Activity3::class.java)
            );

            //implicit intent
        }

        var btn2 = findViewById<TextView>(R.id.forgotPassword)
        btn2.setOnClickListener {
            startActivity(
                Intent(this, Activity5::class.java)
            );
        }

        var email = findViewById<TextView>(R.id.email)
        var pass = findViewById<TextView>(R.id.password)
        var login = findViewById<TextView>(R.id.login)
/*        login.setOnClickListener {
            startActivity(
                Intent(this, Activity7::class.java)
            );
        }*/
        login.setOnClickListener {
            val emailStr = email.text.toString()
            val passStr = pass.text.toString()
            if (emailStr.isNotEmpty() && passStr.isNotEmpty()) {
                mAuth.signInWithEmailAndPassword(emailStr, passStr)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = mAuth.currentUser
                            Toast.makeText(this, "Authentication successful.",
                                Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, Activity7::class.java))
                            finish()
                            // ...
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this, "Authentication failed.",
                               Toast.LENGTH_SHORT).show()
                            // ...
                        }
                    }
                    .addOnFailureListener(this) { exception ->
                        Toast.makeText(this, exception.localizedMessage,
                            Toast.LENGTH_LONG).show()
                    }
            }

        }
        /*if (mAuth.currentUser != null) {
            startActivity(Intent(this, Activity7::class.java))
            finish()
        }*/

    }
}