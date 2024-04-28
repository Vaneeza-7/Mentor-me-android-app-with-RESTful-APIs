package com.vaneezaahmad.i210390

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class Activity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_2)

        var btn = findViewById<TextView>(R.id.signUp)
        btn.setOnClickListener {
            startActivity(
                Intent(this, Activity3::class.java)
            );

            //implicit intent
        }

        var btn2 = findViewById<TextView>(R.id.forgotPassword)
        btn2.setOnClickListener {
            startActivity(
                Intent(this, Activity5::class.java)
            );
        }

        var email = findViewById<EditText>(R.id.email)
        var pass = findViewById<EditText>(R.id.password)
        var login = findViewById<TextView>(R.id.login)
/*        login.setOnClickListener {
            startActivity(
                Intent(this, Activity7::class.java)
            );
        }*/
        //web api request to mentorme
        val url = "http://192.168.100.38/mentorme/login.php"
        login.setOnClickListener {
            val emailStr = email.text.toString()
            val passStr = pass.text.toString()
            if (emailStr.isNotEmpty() && passStr.isNotEmpty()) {
                val request = object : StringRequest(
                    Method.POST, url,
                    { response ->
                        if (response == "1") {
                            startActivity(
                                Intent(this, Activity7::class.java)
                            );
                        } else {
                            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
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
                        return map
                    }
                }
                Volley.newRequestQueue(this).add(request)
            }

        }
        /*if (mAuth.currentUser != null) {
            startActivity(Intent(this, Activity7::class.java))
            finish()
        }*/

    }
}