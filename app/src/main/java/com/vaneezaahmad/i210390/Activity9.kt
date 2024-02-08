package com.vaneezaahmad.i210390

import android.R.layout
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity


class Activity9 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_9)

        val simpleRatingBar = findViewById<View>(R.id.ratingBar) as RatingBar
        simpleRatingBar.numStars = 5
        simpleRatingBar.stepSize = 0.5f
        simpleRatingBar.rating = 2.5f

        val back = findViewById<View>(R.id.arrow)
        back.setOnClickListener {
            startActivity(
                Intent(this, Activity8::class.java)
            );
        }

    }
}