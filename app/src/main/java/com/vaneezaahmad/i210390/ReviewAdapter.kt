package com.vaneezaahmad.i210390

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReviewAdapter(private val reviews: List<Review>) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    class ReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mentorName: TextView = view.findViewById(R.id.mentor_name)
        val rating: RatingBar = view.findViewById(R.id.rating)
        val reviewText: TextView = view.findViewById(R.id.review_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.mentorName.text = review.mentorName
        holder.rating.rating = review.rating
        holder.reviewText.text = review.reviewText
    }

    override fun getItemCount() = reviews.size
}