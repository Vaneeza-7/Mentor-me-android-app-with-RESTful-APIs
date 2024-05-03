package com.vaneezaahmad.i210390

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class ActivityMentor : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentor)

        val extras = intent.extras

        val fragment1 = MentorHomeFragment()
        fragment1.arguments = extras
        val fragment2 = Search_fragment()
        fragment2.arguments = extras
        val fragment3 = MentorChatFragment()
        fragment3.arguments = extras
        val fragment4 = MentorProfileFragment()
        fragment4.arguments = extras

        setCurrentFragment(fragment1)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)

        val plus = findViewById<com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton>(R.id.custom_fab)
        plus.setOnClickListener {
            startActivity(
                Intent(this, Activity10::class.java)
            );
        }

        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> setCurrentFragment(fragment1)
                R.id.navigation_search -> setCurrentFragment(fragment2)
                R.id.navigation_chats -> setCurrentFragment(fragment3)
                R.id.navigation_profile -> setCurrentFragment(fragment4)
            }
            true
        }
    }
    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container_view, fragment)
            commit()
        }
}