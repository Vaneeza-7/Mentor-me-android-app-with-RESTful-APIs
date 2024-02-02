package com.vaneezaahmad.i210390

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
        spinner.setItems("Islamabad", "Karachi", "Lahore", "Quetta", "Rawalpindi", "Peshawar", "Faisalabad", "Gujranwala", "Rahim Yar Khan", "Gujrat")
        spinner.setOnItemSelectedListener { view, position, id, item ->
            Snackbar.make(
                view,
                "Selected $item",
                Snackbar.LENGTH_LONG
            ).show()
        }


    }
}