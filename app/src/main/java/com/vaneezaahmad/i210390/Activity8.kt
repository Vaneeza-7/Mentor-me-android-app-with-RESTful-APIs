package com.vaneezaahmad.i210390

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import de.hdodenhof.circleimageview.CircleImageView

class Activity8 : AppCompatActivity() {
    var mAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_8)

        val name = findViewById<TextView>(R.id.name)
        val role = findViewById<TextView>(R.id.role)
        val description = findViewById<TextView>(R.id.description)
        val image = findViewById<CircleImageView>(R.id.image)

        val mentorName = intent.getStringExtra("mentorName")
        val mentorRole = intent.getStringExtra("mentorRole")
        val mentorDescription = intent.getStringExtra("mentorDescription")
        val mentorImage = intent.getStringExtra("mentorImage")
        val mentorEmail = intent.getStringExtra("mentorEmail")
        val mentorPrice = intent.getStringExtra("mentorPrice")

        name.text = "Hi! I'm " + mentorName
        role.text = mentorRole
        description.text = mentorDescription
        Glide.with(this)
            .load(mentorImage)
            .into(image)

        val back = findViewById<ImageView>(R.id.arrow)
        back.setOnClickListener {
            startActivity(
                Intent(this, Activity7::class.java)
            );

        }

        val review = findViewById<Button>(R.id.review1)
        review.setOnClickListener {
                Intent(this, Activity9::class.java).also {
                    it.putExtra("mentorName", mentorName)
                    it.putExtra("mentorRole", mentorRole)
                    it.putExtra("mentorDescription", mentorDescription)
                    it.putExtra("mentorImage", mentorImage)
                    it.putExtra("mentorEmail", mentorEmail)
                    startActivity(it)
                }
        }

        val book = findViewById<TextView>(R.id.bookSession)
        book.setOnClickListener {

                Intent(this, Activity11::class.java).also{
                    it.putExtra("mentorName", mentorName)
                    it.putExtra("mentorImage", mentorImage)
                    it.putExtra("mentorPrice", mentorPrice)
                    startActivity(it)
                }
        }
    }
}