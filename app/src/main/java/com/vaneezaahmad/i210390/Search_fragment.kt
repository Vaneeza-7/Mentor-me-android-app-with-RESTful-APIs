package com.vaneezaahmad.i210390


import android.R.layout
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment


class Search_fragment : Fragment(R.layout.fragment_search){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val next = view.findViewById<TextView>(R.id.entrepreneurship)

        next.setOnClickListener {
            // Replace the current fragment with the new fragment
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container_view, second_search_fragment())
            fragmentTransaction.addToBackStack(null) // Optional: Add transaction to back stack
            fragmentTransaction.commit()
        }

        val back = view.findViewById<ImageView>(R.id.arrow)

        back.setOnClickListener {
            // Replace the current fragment with the new fragment
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container_view, Home_fragment())
            fragmentTransaction.addToBackStack(null) // Optional: Add transaction to back stack
            fragmentTransaction.commit()
        }
    }
}