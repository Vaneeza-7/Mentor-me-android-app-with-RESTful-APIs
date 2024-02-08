package com.vaneezaahmad.i210390

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.google.android.material.snackbar.Snackbar
import com.jaredrummler.materialspinner.MaterialSpinner

class Activity10 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_10)

        val back = findViewById<ImageView>(R.id.arrow)
        back.setOnClickListener {
            startActivity(
                Intent(this, Activity7::class.java)
            );
        }

        val spinner = findViewById<View>(R.id.status) as MaterialSpinner
        spinner.setItems("Status","Available", "Busy", "Away", "Offline")
        spinner.setOnItemSelectedListener { view, position, id, item ->
            Snackbar.make(
                view,
                "Selected $item",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }
}