package com.vaneezaahmad.i210390

import android.content.Intent
import android.os.Bundle
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class Activity7 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_7)

        val fragment1 = Home_fragment()
        val fragment2 = Search_fragment()
        val fragment3 = Chat_fragment()
        val fragment4 = Profile_fragment()

        setCurrentFragment(fragment1)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> setCurrentFragment(fragment1)
                R.id.navigation_search -> setCurrentFragment(fragment2)
                R.id.navigation_chats -> setCurrentFragment(fragment3)
                R.id.navigation_profile -> setCurrentFragment(fragment4)
            }
            true
        }

        /*val plus = findViewById<FloatingActionButton>(R.id.fab)
        plus.setOnClickListener {
            startActivity(
                Intent(this, Activity8::class.java)
            );
        }*/

        val plus = findViewById<com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton>(R.id.custom_fab)
        plus.setOnClickListener {
            startActivity(
                Intent(this, Activity10::class.java)
            );
        }

    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container_view, fragment)
            commit()
        }
}