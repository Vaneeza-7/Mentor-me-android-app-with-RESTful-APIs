package com.vaneezaahmad.i210390

import android.R.layout
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import de.hdodenhof.circleimageview.CircleImageView


class Activity9 : AppCompatActivity() {
    var mAuth = FirebaseAuth.getInstance()
    var database = Firebase.database;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_9)

        var name = findViewById<TextView>(R.id.name)
        var image = findViewById<CircleImageView>(R.id.image)
        var review = findViewById<EditText>(R.id.review)

        val mentorName = intent.getStringExtra("mentorName")
        val mentorImage = intent.getStringExtra("mentorImage")

        name.text = "Hi! I'm " + mentorName
        Glide.with(this)
            .load(mentorImage)
            .into(image)

        val simpleRatingBar = findViewById<View>(R.id.ratingBar) as RatingBar
        simpleRatingBar.numStars = 5
        simpleRatingBar.stepSize = 0.5f
        simpleRatingBar.rating = 2.5f

        simpleRatingBar.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { ratingBar, rating, fromUser ->
            Toast.makeText(applicationContext, "Your Selected Ratings  : $rating", Toast.LENGTH_SHORT).show()
        }

        val back = findViewById<View>(R.id.arrow)
        back.setOnClickListener {
            startActivity(
                Intent(this, Activity8::class.java)
            );
        }

        val submit = findViewById<View>(R.id.submit)
        submit.setOnClickListener {
            Toast.makeText(applicationContext, "Review Submitted", Toast.LENGTH_SHORT).show()
            if (mentorName != null) {
                var review = Review(mentorName, simpleRatingBar.rating, review.text.toString())
                mAuth.currentUser?.uid?.let { it1 -> database.getReference("reviews").child(it1).setValue(review) }
                finish()
            }
        }

    }
}