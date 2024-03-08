package com.vaneezaahmad.i210390

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider

class Activity4 : AppCompatActivity() {
    private val otp = arrayOfNulls<String>(6)
    private var otpIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_4)

        var back = findViewById<ImageView>(R.id.imageView1)
        back.setOnClickListener {
            startActivity(
                Intent(this, Activity3::class.java)
            );
        }

        var next = findViewById<TextView>(R.id.submit)
        back.setOnClickListener {
            startActivity(
                Intent(this, Activity7::class.java)
            );
        }


        var circle1 = findViewById<TextView>(R.id.circle1)
        var circle2 = findViewById<TextView>(R.id.circle2)
        var circle3 = findViewById<TextView>(R.id.circle3)
        var circle4 = findViewById<TextView>(R.id.circle4)
        var circle5 = findViewById<TextView>(R.id.circle5)
        var circle6 = findViewById<TextView>(R.id.circle6)
        val circles = arrayOf(circle1, circle2, circle3, circle4, circle5, circle6)
        var one = findViewById<TextView>(R.id.num1)
        var two = findViewById<TextView>(R.id.num2)
        var three = findViewById<TextView>(R.id.num3)
        var four = findViewById<TextView>(R.id.num4)
        var five = findViewById<TextView>(R.id.num5)
        var six = findViewById<TextView>(R.id.num6)
        var seven = findViewById<TextView>(R.id.num7)
        var eight = findViewById<TextView>(R.id.num8)
        var nine = findViewById<TextView>(R.id.num9)
        var zero = findViewById<TextView>(R.id.num0)
        var backspace = findViewById<ImageView>(R.id.backspace)

        one.setOnClickListener {
            appendNumber("1", circles)
        }
        two.setOnClickListener {
            appendNumber("2", circles)
        }
        three.setOnClickListener {
            appendNumber("3", circles)
        }
        four.setOnClickListener {
            appendNumber("4", circles)
        }
        five.setOnClickListener {
            appendNumber("5", circles)
        }
        six.setOnClickListener {
            appendNumber("6", circles)
        }
        seven.setOnClickListener {
            appendNumber("7", circles)
        }
        eight.setOnClickListener {
            appendNumber("8", circles)
        }
        nine.setOnClickListener {
            appendNumber("9", circles)
        }
        zero.setOnClickListener {
            appendNumber("0", circles)
        }
        backspace.setOnClickListener {
            if(otpIndex > 0) {
                circles[otpIndex - 1].setText("")
                otpIndex--
            }
        }
        val token = intent.getStringExtra("token")
        val otpStr = otp.joinToString("") // convert the OTP array to a string

        var btn = findViewById<TextView>(R.id.submit)
        btn.setOnClickListener {
            var credential =PhoneAuthProvider.getCredential(token!!, otpStr)
            var auth = FirebaseAuth.getInstance()
            auth.signInWithCredential(credential)
                .addOnSuccessListener {
                    var i = Intent (this, Activity7::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(i)
                }

                .addOnFailureListener {
                    Toast.makeText(this, "Failed to login", LENGTH_LONG).show()
                    Log.d("Failed to login", it.localizedMessage)
                }
        }
    }
    fun appendNumber (number : String, circles : Array<TextView>) {
        if(otpIndex < 6) {
            otp[otpIndex] = number
            circles[otpIndex].setText(number)
            otpIndex++
        }
    }
}