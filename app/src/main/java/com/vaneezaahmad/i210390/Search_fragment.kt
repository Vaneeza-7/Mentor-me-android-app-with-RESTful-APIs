package com.vaneezaahmad.i210390


import android.R.layout
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class Search_fragment : Fragment(R.layout.fragment_search){

    private lateinit var adapter: SearchListAdapter
    private val mentors = ArrayList<Mentor>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val firebaseRef2 = FirebaseDatabase.getInstance().getReference("Mentors")
        firebaseRef2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (mentorSnapshot in dataSnapshot.children) {
                        val mentor = mentorSnapshot.getValue(Mentor::class.java)
                        if (mentor != null) {
                            mentors.add(mentor)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        adapter = SearchListAdapter(mentors)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        val searchView = view.findViewById<android.widget.SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val sharedPreferences = requireContext().getSharedPreferences("recent_searches", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                val searches = sharedPreferences.getStringSet("searches", mutableSetOf()) ?: mutableSetOf()
                searches.add(query)
                editor.putStringSet("searches", searches)
                editor.apply()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = mentors.filter { it.name.contains(newText ?: "", ignoreCase = true) }
                adapter.filterList(filteredList)
                return false
            }
        })

        val sharedPreferences = requireContext().getSharedPreferences("recent_searches", Context.MODE_PRIVATE)
        val searches = sharedPreferences.getStringSet("searches", null)

        val mentor1 = view.findViewById<TextView>(R.id.mentor1)
        val mentor2 = view.findViewById<TextView>(R.id.mentor2)
        val mentor3 = view.findViewById<TextView>(R.id.mentor3)

        val searchList = searches?.toList()
        mentor1.text = searchList?.getOrNull(0)
        mentor2.text = searchList?.getOrNull(1)
        mentor3.text = searchList?.getOrNull(2)

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