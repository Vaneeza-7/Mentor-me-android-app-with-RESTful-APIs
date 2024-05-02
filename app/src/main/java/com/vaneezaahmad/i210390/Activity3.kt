package com.vaneezaahmad.i210390

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
//import com.google.firebase.auth.PhoneAuthProvider
import com.hbb20.CountryPickerView
import com.hbb20.countrypicker.models.CPCountry
import com.jaredrummler.materialspinner.MaterialSpinner
import org.json.JSONObject


class Activity3 : AppCompatActivity() {
    //var mAuth = FirebaseAuth.getInstance()
    //val database = Firebase.database;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_3)

        val name = findViewById<EditText>(R.id.editTextTextPersonName)
        val email = findViewById<EditText>(R.id.editEmailAddress)
        val password = findViewById<EditText>(R.id.editTextTextPassword)
        val phone = findViewById<EditText>(R.id.editTextPhone)
        var selectedCity: String = ""
        var countryName: String = ""
        val countryPicker = findViewById<CountryPickerView>(R.id.spinner)

        countryPicker.cpViewHelper.onCountryChangedListener = { selectedCountry: CPCountry? ->
            // your code to handle selected country
            selectedCountry?.let {
                Snackbar.make(
                    countryPicker,
                    "Selected ${it.name}",
                    Snackbar.LENGTH_LONG
                ).show()
                 countryName = selectedCountry?.name ?: "Unknown"
                countryName = countryName.toString();

            }
        }

        val spinner = findViewById<View>(R.id.cityspinner) as MaterialSpinner
        spinner.setItems("Select City","Islamabad", "Karachi", "Lahore", "Quetta", "Rawalpindi", "Peshawar", "Faisalabad", "Gujranwala", "Rahim Yar Khan", "Gujrat")
        spinner.setOnItemSelectedListener { view, position, id, item ->
            Snackbar.make(
                view,
                "Selected $item",
                Snackbar.LENGTH_LONG
            ).show()
            selectedCity = item.toString()
        }

        /*val spinner2 = findViewById<View>(R.id.spinner) as MaterialSpinner
        spinner2.setItems("Select Country","United States", "Canada", "United Kingdom", "Germany", "France",
            "Japan", "Australia", "India", "Brazil", "South Africa", "Pakistan",
            "China", "Russia", "Mexico", "Italy", "Spain", "South Korea", "Indonesia", "Netherlands", "Saudi Arabia", "Turkey")
        spinner2.setOnItemSelectedListener { view, position, id, item ->
            Snackbar.make(
                view,
                "Selected $item",
                Snackbar.LENGTH_LONG
            ).show()
        }*/

        val url = getString(R.string.IP) + "mentorme/insert.php"
        var button = findViewById<View>(R.id.button)
        button.setOnClickListener {
            val nameStr = name.text.toString()
            val emailStr = email.text.toString()
            val passStr = password.text.toString()
            val phoneStr = phone.text.toString()

            if (nameStr.isNotEmpty() && emailStr.isNotEmpty() && passStr.isNotEmpty() && phoneStr.isNotEmpty() && selectedCity.isNotEmpty() && countryName.isNotEmpty()) {
                val request = object : StringRequest(
                    Method.POST, url,
                    { response ->
                        val JsonResponse = JSONObject(response)
                        val status = JsonResponse.getInt("status")
                        if (status == 1) {
                            //phoneSignup(phoneStr)
                            startActivity(
                                Intent(this, Activity7::class.java)
                            );
                            Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Couldn't register user", Toast.LENGTH_SHORT).show()
                        }
                    },
                    { error ->
                        Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                    }
                ) {
                    override fun getParams(): MutableMap<String, String> {
                        val map = HashMap<String, String>()
                        map["email"] = emailStr
                        map["password"] = passStr
                        map["name"] = nameStr
                        map["phone"] = phoneStr
                        map["country"] = countryName
                        map["city"] = selectedCity
                        return map
                    }
                }
                Volley.newRequestQueue(this).add(request)
            }
        }

        var back = findViewById<View>(R.id.textView11)
        back.setOnClickListener {
            startActivity(
                Intent(this, Activity2::class.java)
            );
        }
    }

    fun phoneSignup (phoneStr: String) {

///*        var callbacks = object :PhoneAuthProvider.OnVerificationStateChangedCallbacks()
//        {
//            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
//                Toast.makeText(this@Activity3, "Verification completed", Toast.LENGTH_SHORT).show()
//                startActivity(Intent(this@Activity3, Activity7::class.java) )
//
//            }
//
//            override fun onVerificationFailed(p0: FirebaseException) {
//                Toast.makeText(this@Activity3, "Verification failed", Toast.LENGTH_SHORT).show()
//                Log.d("Verification failed", p0.localizedMessage)
//            }
//
//            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
//                super.onCodeSent(p0, p1)
//                var i = Intent(this@Activity3, Activity4::class.java)
//                i.putExtra("token", p0)
//                i.putExtra("phone", phoneStr)
//                startActivity(i)
//                Log.d("Code sent", "Code sent" )
//                Toast.makeText(this@Activity3, "Code sent", Toast.LENGTH_SHORT).show()
//            }
//
//        }
//        var options = PhoneAuthOptions.newBuilder()
//            .setPhoneNumber(phoneStr)
//            .setTimeout(60L, java.util.concurrent.TimeUnit.SECONDS)
//            .setActivity(this)
//            .setCallbacks(callbacks)
//            .build()
//        PhoneAuthProvider.verifyPhoneNumber(options)*/
    }
}
