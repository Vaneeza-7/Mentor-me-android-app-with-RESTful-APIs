package com.vaneezaahmad.i210390

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import com.hbb20.CountryPickerView
import com.hbb20.countrypicker.models.CPCountry

class Activity16 : AppCompatActivity() {
    var mAuth = FirebaseAuth.getInstance()
    var ref = Firebase.database.getReference("users")
    var uid = mAuth.currentUser?.uid.toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_16)

        val back = findViewById<ImageButton>(R.id.arrow)
        back.setOnClickListener{
            finish()
        }

        val editname = findViewById<EditText>(R.id.editname)
        val editemail = findViewById<EditText>(R.id.editemail)
        val editphone = findViewById<EditText>(R.id.editphone)
        val city = findViewById<EditText>(R.id.editcity)
        val countryPicker = findViewById<CountryPickerView>(R.id.countryPicker)
        val update = findViewById<TextView>(R.id.update)

        ref.child(uid).get().addOnSuccessListener {
            if (it.exists()) {
                val name = it.child("name").value.toString()
                val email = it.child("email").value.toString()
                val phone = it.child("phone").value.toString()
                val cityname = it.child("city").value.toString()
                val countryname = it.child("country").value.toString()

                editname.setText(name)
                editemail.setText(email)
                editphone.setText(phone)
                city.setText(cityname)
                countryPicker.cpViewHelper.selectedCountry

                /*                countryPicker.cpViewHelper.onCountryChangedListener = { selectedCountry ->

                // Set the selected country in the CountryPickerView
                selectedCountry?.let {
                    countryPicker.setCountry(selectedCountry)
                }
                country.tvCountryInfo.setText(countryname)

            }*/
            }
        }.addOnFailureListener{
            Snackbar.make(
                editname,
                "Error getting data",
                Snackbar.LENGTH_LONG
            ).show()
        }

        update.setOnClickListener {
            val name = editname.text.toString()
            val email = editemail.text.toString()
            val phone = editphone.text.toString()
            val cityname = city.text.toString()
            var countryName: String = ""

            countryPicker.cpViewHelper.onCountryChangedListener = { selectedCountry: CPCountry? ->
                // your code to handle selected country
                selectedCountry?.let {
                    countryName = selectedCountry?.name ?: "Unknown"

                }
            }

            ref.child(uid).child("name").setValue(name)
            ref.child(uid).child("email").setValue(email)
            ref.child(uid).child("phone").setValue(phone)
            ref.child(uid).child("city").setValue(cityname)
            ref.child(uid).child("country").setValue(countryName)

            Toast.makeText(this, "Data updated successfully", Toast.LENGTH_SHORT).show()
        }
    }
}