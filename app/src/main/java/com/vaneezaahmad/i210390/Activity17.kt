package com.vaneezaahmad.i210390

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class Activity17 : AppCompatActivity() {
    var mAuth = FirebaseAuth.getInstance()
    var database = Firebase.database
    var uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_17)

        val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)

        val bookingsRef = database.getReference("bookings")
        val bookings = mutableListOf<Booking>()

        bookingsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (bookingSnapshot in dataSnapshot.children) {
                        val booking = bookingSnapshot.getValue(Booking::class.java)
                        if (booking != null) {
                            if (booking.userId == uid) {
                                bookings.add(booking)
                            }
                        }
                    }
                    recyclerView.adapter = BookingAdapter(bookings)
                    recyclerView.adapter?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        val back = findViewById<ImageButton>(R.id.backButton)
        back.setOnClickListener{

            finish();
        }
    }
}