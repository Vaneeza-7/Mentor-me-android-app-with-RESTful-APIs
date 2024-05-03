package com.vaneezaahmad.i210390

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONObject
import java.util.Calendar


class Activity11 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_11)

        val name = findViewById<TextView>(R.id.heading)
        val price = findViewById<TextView>(R.id.price)
        val image = findViewById<CircleImageView>(R.id.profile_pic)
        val calendarView = findViewById<com.applandeo.materialcalendarview.CalendarView>(R.id.calendarView)
        val timeSlot1 = findViewById<TextView>(R.id.timeSlot1)
        val timeSlot2 = findViewById<TextView>(R.id.timeSlot2)
        val timeSlot3 = findViewById<TextView>(R.id.timeSlot3)
        val book = findViewById<TextView>(R.id.book)

        val mentorName = intent.getStringExtra("mentorName")
        val mentorImage = intent.getStringExtra("mentorImage")
        val mentorPrice = intent.getStringExtra("mentorPrice")
        val mentorEmail = intent.getStringExtra("mentorEmail")
        val email = intent.getStringExtra("videoDisguised")
        var time : String = ""
        var date :String = ""

        name.text = mentorName
        price.text = mentorPrice
        Glide.with(this)
            .load(mentorImage)
            .into(image)


        calendarView.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                calendarView.setSelectionBackground(R.color.white)
                val clickedDayCalendar: Calendar = eventDay.calendar
                val day = clickedDayCalendar.get(Calendar.DAY_OF_MONTH)
                val month = clickedDayCalendar.get(Calendar.MONTH)
                val year = clickedDayCalendar.get(Calendar.YEAR)
                date = "$day/$month/$year"
                Toast.makeText(this@Activity11, "$day/$month/$year", Toast.LENGTH_LONG).show()
            }
        })

        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            finish();
        }

        timeSlot1.setOnClickListener {
            time = "10:00 AM"
            Toast.makeText(this, "10:00 AM Selected", Toast.LENGTH_SHORT).show()
        }

        timeSlot2.setOnClickListener {
            time = "11:00 AM"
            Toast.makeText(this, "12:00 PM Selected", Toast.LENGTH_SHORT).show()
        }

        timeSlot3.setOnClickListener {
            time = "12:00 PM"
            Toast.makeText(this, "2:00 PM Selected", Toast.LENGTH_SHORT).show()
        }

        book.setOnClickListener {
            val booking = Booking(
                "Vaneeza Ahmad",
                mentorName!!,
                mentorPrice!!,
                mentorImage!!,
                date,
                time
            )
            if(email!=null && mentorEmail !=null && date != "" && time != "" && mentorName != null && mentorPrice != null && mentorImage != null){
                val url = getString(R.string.IP) + "mentorme/bookSession.php"
                val request = object : StringRequest(
                    Method.POST, url,
                    { response ->
                        val JsonResponse = JSONObject(response)
                        val status = JsonResponse.getInt("status")
                        if (status == 1) {
                            Toast.makeText(this, "Session Booked", Toast.LENGTH_SHORT).show()
                            finish();
                        } else {
                            Toast.makeText(this, "Session not Booked", Toast.LENGTH_SHORT).show()
                        }
                    },
                    { error ->
                        Toast.makeText(this, "error.message", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    override fun getParams(): MutableMap<String, String> {
                        val map = HashMap<String, String>()
                        map["useremail"] = email
                        map["mentoremail"] = mentorEmail
                        map["mentorname"] = mentorName
                        map["date"] = date
                        map["time"] = time
                        map["price"] = mentorPrice
                        map["mentorimage"] = mentorImage
                        return map
                    }
                }
                val queue = Volley.newRequestQueue(this)
                queue.add(request)
            }
            else{
                Toast.makeText(this, "Please select date and time", Toast.LENGTH_SHORT).show()

            }
        }
    }
}