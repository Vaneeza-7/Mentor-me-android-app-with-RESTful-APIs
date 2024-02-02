package com.vaneezaahmad.i210390

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.jaredrummler.materialspinner.MaterialSpinner


class Activity3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_3)

        val spinner = findViewById<View>(R.id.cityspinner) as MaterialSpinner
        spinner.setItems("Select City","Islamabad", "Karachi", "Lahore", "Quetta", "Rawalpindi", "Peshawar", "Faisalabad", "Gujranwala", "Rahim Yar Khan", "Gujrat")
        spinner.setOnItemSelectedListener { view, position, id, item ->
            Snackbar.make(
                view,
                "Selected $item",
                Snackbar.LENGTH_LONG
            ).show()
        }

        val spinner2 = findViewById<View>(R.id.spinner) as MaterialSpinner
        spinner2.setItems("Select Country","United States", "Canada", "United Kingdom", "Germany", "France",
            "Japan", "Australia", "India", "Brazil", "South Africa", "Pakistan",
            "China", "Russia", "Mexico", "Italy", "Spain", "South Korea", "Indonesia", "Netherlands", "Saudi Arabia", "Turkey")
        spinner2.setOnItemSelectedListener { view, position, id, item ->
            Snackbar.make(
                view,
                "Selected $item",
                Snackbar.LENGTH_LONG
            ).show()
        }

        var button = findViewById<View>(R.id.button)
        button.setOnClickListener {
            startActivity(
                Intent(this, Activity4::class.java)
            );
        }

        var back = findViewById<View>(R.id.textView11)
        back.setOnClickListener {
            startActivity(
                Intent(this, Activity2::class.java)
            );
        }
    }
}