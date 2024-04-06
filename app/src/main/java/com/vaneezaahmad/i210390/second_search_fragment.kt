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
import com.google.firebase.database.FirebaseDatabase
import com.jaredrummler.materialspinner.MaterialSpinner

class second_search_fragment : Fragment(R.layout.second_fragment_search) {
val database = FirebaseDatabase.getInstance()
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

        val mentors = ArrayList<Mentor>()
        val mentorRef = database.getReference("Mentors")
        mentorRef.get().addOnSuccessListener {
            for (mentor in it.children) {
                val mentorObj = mentor.getValue(Mentor::class.java)
                if (mentorObj != null) {
                    mentors.add(mentorObj)
                }
            }
            val recycler_view = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recycler_view)
            recycler_view.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
            recycler_view.adapter = SearchMentorAdapter(mentors)
        }
    }
}