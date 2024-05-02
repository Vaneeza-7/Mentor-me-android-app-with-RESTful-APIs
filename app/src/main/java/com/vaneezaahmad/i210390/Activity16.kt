package com.vaneezaahmad.i210390

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.hbb20.CountryPickerView
import com.hbb20.countrypicker.models.CPCountry
import org.json.JSONObject

class Activity16 : AppCompatActivity() {
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
        val getExtra = intent.getStringExtra("email")

        var url = getString(R.string.IP) + "mentorme/getUserData.php"
        val request = object : StringRequest(
            Method.POST, url,
            { response ->
                val jsonResponse = JSONObject(response)
                if (jsonResponse.has("name") && jsonResponse.has("city") && jsonResponse.has("phone")&& jsonResponse.has("country") ){
                    val name = jsonResponse.getString("name")
                    val city = jsonResponse.getString("city")
                    val phone = jsonResponse.getString("phone")
                    val password = jsonResponse.getString("password")

                    if(name == "null") {
                        findViewById<TextView>(R.id.name).text = "User"
                    }
                    else {
                        findViewById<TextView>(R.id.editname).text = name
                        findViewById<TextView>(R.id.editemail).text = getExtra
                        findViewById<TextView>(R.id.editphone).text = phone
                        findViewById<TextView>(R.id.editcity).text = city

                    }

                } else {

                    Toast.makeText(this, "Not complete JsonObject", Toast.LENGTH_SHORT).show()
                }

            },
            { error ->
                Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val map = HashMap<String, String>()
                map["email"] = getExtra.toString()
                return map
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)

        update.setOnClickListener {
            val name = editname.text.toString()
            val phone = editphone.text.toString()
            val cityname = city.text.toString()
            var countryName: String = ""

            countryPicker.cpViewHelper.onCountryChangedListener = { selectedCountry: CPCountry? ->
                // your code to handle selected country
                selectedCountry?.let {
                    countryName = selectedCountry?.name ?: "Unknown"

                }
            }

            var url2 = getString(R.string.IP) + "mentorme/updateUserData.php"
            val request2 = object : StringRequest(
                Method.POST, url2,
                { response ->
                    val jsonResponse = JSONObject(response)
                    val status = jsonResponse.getString("status")
                    if (status == "success") {
                        Toast.makeText(this, "Data updated successfully", Toast.LENGTH_SHORT).show()
                        finish();
                    } else {
                        Toast.makeText(this, "Data not updated", Toast.LENGTH_SHORT).show()
                    }
                },
                { error ->
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                }
            ) {
                override fun getParams(): MutableMap<String, String> {
                    val map = HashMap<String, String>()
                    map["name"] = name
                    map["email"] = getExtra.toString()
                    map["phone"] = phone
                    map["city"] = cityname
                    map["country"] = countryName
                    return map
                }
            }
            val queue2 = Volley.newRequestQueue(this)
            queue2.add(request2)
        }
    }
}