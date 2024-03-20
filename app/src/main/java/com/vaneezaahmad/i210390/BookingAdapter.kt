package com.vaneezaahmad.i210390

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database

class BookingAdapter (private var bookings: List<Booking>): RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    inner class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mentorName: TextView = itemView.findViewById(R.id.name)
        val mentorPrice: TextView = itemView.findViewById(R.id.price)
        val date: TextView = itemView.findViewById(R.id.date)
        val time: TextView = itemView.findViewById(R.id.time)
        val image: ImageView = itemView.findViewById(R.id.image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_booking, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = bookings[position]
        holder.mentorName.text = booking.mentorName
        holder.mentorPrice.text = booking.mentorPrice
        holder.date.text = booking.date
        holder.time.text = booking.time
        Glide.with(holder.itemView)
            .load(booking.mentorImage)
            .into(holder.image)
    }

    override fun getItemCount(): Int {
        return bookings.size
    }
}