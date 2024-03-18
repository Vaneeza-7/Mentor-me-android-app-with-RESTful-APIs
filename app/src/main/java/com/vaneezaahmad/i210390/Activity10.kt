package com.vaneezaahmad.i210390

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.jaredrummler.materialspinner.MaterialSpinner

class Activity10 : AppCompatActivity() {
    var mAuth = FirebaseAuth.getInstance();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_10)

        val name = findViewById<EditText>(R.id.name)
        val email = findViewById<EditText>(R.id.email)
        val role = findViewById<EditText>(R.id.role)
        val desc = findViewById<EditText>(R.id.description)
        val price = findViewById<EditText>(R.id.price)
        val pass = findViewById<EditText>(R.id.password)
        val upload = findViewById<TextView>(R.id.upload)

        val back = findViewById<ImageView>(R.id.arrow)
        back.setOnClickListener {
           /* startActivity(
                Intent(this, Activity7::class.java)
            );*/
            finish();
        }

        val login = findViewById<TextView>(R.id.login)
        login.setOnClickListener {
            startActivity(
                Intent(this, MentorLogin::class.java)
            );
        }

        val spinner = findViewById<View>(R.id.status) as MaterialSpinner
        spinner.setItems("Status","Available", "Busy", "Away", "Offline")
        spinner.setOnItemSelectedListener { view, position, id, item ->
            Snackbar.make(
                view,
                "Selected $item",
                Snackbar.LENGTH_LONG
            ).show()
        }

        val spinner_category = findViewById<View>(R.id.category) as MaterialSpinner
        spinner_category.setItems("Education","Entrepreneurship", "Personal Growth", "Career Development", "Technology", "Health", "Music", "Sports", "Cooking", "Fashion", "Other")
        spinner_category.setOnItemSelectedListener { view, position, id, item ->
            Snackbar.make(
                view,
                "Selected $item",
                Snackbar.LENGTH_LONG
            ).show()
        }

        val nextvid = findViewById<ImageView>(R.id.mentor)
        /*nextvid.setOnClickListener {
            startActivity(
                Intent(this, Activity13::class.java)
            );
        }*/

        val nextcam = findViewById<ImageView>(R.id.camera)
        /*nextcam.setOnClickListener {
            startActivity(
                Intent(this, Activity12::class.java)
            );
        }*/

        upload.setOnClickListener {
            val nameStr = name.text.toString()
            val emailStr = email.text.toString()
            val passStr = pass.text.toString()
            val roleStr = role.text.toString()
            val descStr = desc.text.toString()
            val priceStr = price.text.toString()
            val statusStr = spinner.text.toString()
            val categoryStr = spinner_category.text.toString()

            if (nameStr.isNotEmpty() && emailStr.isNotEmpty() && passStr.isNotEmpty() && roleStr.isNotEmpty() && descStr.isNotEmpty() && priceStr.isNotEmpty() && statusStr.isNotEmpty() && categoryStr.isNotEmpty()) {
                mAuth.createUserWithEmailAndPassword(emailStr, passStr)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = mAuth.currentUser
                            Snackbar.make(
                                upload,
                                "Authentication successful.",
                                Snackbar.LENGTH_LONG
                            ).show()
                            startActivity(Intent(this, Activity7::class.java))
                            finish()
                            // ...
                        } else {
                            // If sign in fails, display a message to the user.
                            Snackbar.make(
                                upload,
                                "Authentication failed.",
                                Snackbar.LENGTH_LONG
                            ).show()
                            // ...
                        }
                    }
                    .addOnFailureListener(this) { exception ->
                        Snackbar.make(
                            upload,
                            exception.localizedMessage,
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
            }

        }
    }
}