package com.vaneezaahmad.i210390

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.jaredrummler.materialspinner.MaterialSpinner

class second_search_fragment : Fragment(R.layout.second_fragment_search) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val back = view.findViewById<ImageView>(R.id.arrow)

        back.setOnClickListener {
            // Replace the current fragment with the new fragment
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container_view, Search_fragment())
            fragmentTransaction.addToBackStack(null) // Optional: Add transaction to back stack
            fragmentTransaction.commit()
        }

        val spinner = view.findViewById<View>(R.id.filter) as MaterialSpinner
        spinner.setItems("Filter","Price: Low to High", "Price: High to Low", "Rating: Low to High", "Rating: High to Low")
        spinner.setOnItemSelectedListener { view, position, id, item ->
            Snackbar.make(
                view,
                "Selected $item",
                Snackbar.LENGTH_LONG
            ).show()
        }


        /*view.findViewById<CardView>(R.id.card).setOnClickListener {
            val intent = Intent(requireContext(), Activity8::class.java)
            startActivity(intent)
        }*/

        val recycler_view = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recycler_view)
        recycler_view.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        recycler_view.adapter = SearchMentorAdapter(listOf(
            Mentor("Vaneeza Ahmad", "Rs. 1000", "Entrepreneur", "Available", R.drawable.card, "Entrepreneurship"),
            Mentor("Ali Khan", "Rs. 1500", "Engineer", "Available", R.drawable.card, "Engineering"),
            Mentor("Sara Khan", "Rs. 2000", "Doctor", "Available", R.drawable.card, "Medicine"),
            Mentor("Ahmed Khan", "Rs. 2500", "Designer", "Available", R.drawable.card, "Designing"),
            Mentor("Sana Khan", "Rs. 3000", "Developer", "Available", R.drawable.card, "Development"),
            Mentor("Zain Khan", "Rs. 3500", "Artist", "Available", R.drawable.card, "Art"),
            Mentor("Zara Khan", "Rs. 4000", "Teacher", "Available", R.drawable.card, "Teaching"),
            Mentor("Zoha Khan", "Rs. 4500", "Chef", "Available", R.drawable.card, "Cooking"),
            Mentor("Zainab Khan", "Rs. 5000", "Photographer", "Available", R.drawable.card, "Photography"),
            Mentor("Zain Khan", "Rs. 5500", "Artist", "Available", R.drawable.card, "Art"),
            Mentor("Zara Khan", "Rs. 6000", "Teacher", "Available", R.drawable.card, "Teaching"),
            Mentor("Zoha Khan", "Rs. 6500", "Chef", "Available", R.drawable.card, "Cooking"),
            Mentor("Zainab Khan", "Rs. 7000", "Photographer", "Available", R.drawable.card, "Photography"),
            Mentor("Zain Khan", "Rs. 7500", "Artist", "Available", R.drawable.card, "Art"),
            Mentor("Zara Khan", "Rs. 8000", "Teacher", "Available", R.drawable.card, "Teaching"),
            Mentor("Zoha Khan", "Rs. 8500", "Chef", "Available", R.drawable.card, "Cooking"),
            Mentor("Zainab Khan", "Rs. 9000", "Photographer", "Available", R.drawable.card, "Photography")))


    }
}