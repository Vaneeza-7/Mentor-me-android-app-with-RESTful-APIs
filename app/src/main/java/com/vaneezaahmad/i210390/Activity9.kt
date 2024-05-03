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
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONObject


class Activity9 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_9)

        var name = findViewById<TextView>(R.id.name)
        var image = findViewById<CircleImageView>(R.id.image)
        var review = findViewById<EditText>(R.id.review)

        val mentorName = intent.getStringExtra("mentorName")
        val mentorImage = intent.getStringExtra("mentorImage")
        val mentorEmail = intent.getStringExtra("mentorEmail")
        val videoDisguised = intent.getStringExtra("videoDisguised")

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
            finish();
        }

        val submit = findViewById<View>(R.id.submit)
        submit.setOnClickListener {
            if (mentorName != null && simpleRatingBar.rating != null && review.text != null && mentorEmail != null && videoDisguised != null) {
                //var review = Review(mentorName, simpleRatingBar.rating, review.text.toString())
                val reviewText = review.text.toString()
                val url = getString(R.string.IP) + "mentorme/addreview.php"
                val request = object : StringRequest(
                    Method.POST, url,
                    { response ->
                        val JsonResponse = JSONObject(response)
                        val status = JsonResponse.getInt("status")
                        if (status == 1) {
                            Toast.makeText(this, "Review Submitted", Toast.LENGTH_SHORT).show()
                            finish();
                        } else {
                            Toast.makeText(this, "Review not Submitted", Toast.LENGTH_SHORT).show()
                        }
                    },
                    { error ->
                        Toast.makeText(this, "error.message", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    override fun getParams(): MutableMap<String, String> {
                        val map = HashMap<String, String>()
                        map["useremail"] = videoDisguised
                        map["mentoremail"] = mentorEmail
                        map["mentorname"] = mentorName
                        map["rating"] = simpleRatingBar.rating.toString()
                        map["text"] = reviewText
                        return map
                    }
                }
                Volley.newRequestQueue(this).add(request)

            }
            else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

    }
}