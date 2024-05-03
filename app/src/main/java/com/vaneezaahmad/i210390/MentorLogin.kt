package com.vaneezaahmad.i210390

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class MentorLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentor_login)

        var email = findViewById<EditText>(R.id.email)
        var pass = findViewById<EditText>(R.id.password)
        var login = findViewById<TextView>(R.id.login)

        val signUp = findViewById<TextView>(R.id.signUp)
        signUp.setOnClickListener {
         finish();
        }

        login.setOnClickListener {
            val emailStr = email.text.toString()
            val passStr = pass.text.toString()
            if (emailStr.isNotEmpty() && passStr.isNotEmpty()) {
                val url = getString(R.string.IP) + "mentorme/mentorLogin.php"
                val request = object : StringRequest(
                    Method.POST, url,
                    { response ->
                        //val jsonResponse = JSONObject(response)
                        //val status = jsonResponse.getInt("status")
                        if (response == "1") {
                            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                            var intent = Intent(this, ActivityMentor::class.java)
                            intent.putExtra("email", emailStr)
                            startActivity(intent)
                        } else
                        {
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

    }
}