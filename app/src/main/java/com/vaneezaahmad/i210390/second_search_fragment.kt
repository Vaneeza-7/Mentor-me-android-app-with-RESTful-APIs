package com.vaneezaahmad.i210390

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.jaredrummler.materialspinner.MaterialSpinner
import org.json.JSONArray

class second_search_fragment : Fragment(R.layout.second_fragment_search) {

    private lateinit var adapter: SearchMentorAdapter
    private var mentors = ArrayList<Mentor>()
    private lateinit var recyclerView: RecyclerView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val back = view.findViewById<ImageView>(R.id.arrow)
        val email = arguments?.getString("email").toString()

        back.setOnClickListener {
            // Replace the current fragment with the new fragment
//            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
//            fragmentTransaction.replace(R.id.fragment_container_view, Search_fragment())
//            fragmentTransaction.addToBackStack(null) // Optional: Add transaction to back stack
//            fragmentTransaction.commit()

            requireActivity().supportFragmentManager.popBackStack()
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
        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = SearchMentorAdapter(mentors)
        recyclerView.adapter = adapter
        fetchMentors(getString(R.string.IP) + "mentorme/getMentors.php", email)

    }

    fun fetchMentors(url: String, email: String?) {
        val requestQueue = Volley.newRequestQueue(context)
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val mentors = parseMentors(response, email.toString())
                this.mentors = mentors as ArrayList<Mentor>
                adapter = SearchMentorAdapter(mentors)
                recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(context, "Could not fetch mentors", Toast.LENGTH_SHORT).show()
            }
        )

        requestQueue.add(jsonArrayRequest)
    }

    fun parseMentors(jsonArray: JSONArray, email: String?): List<Mentor> {
        val mentors = mutableListOf<Mentor>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val dpUrl = jsonObject.getString("dp")
            val timestamp = System.currentTimeMillis()
            val dpUrlWithTimestamp = if (dpUrl.isNotEmpty()) "$dpUrl?timestamp=$timestamp" else dpUrl

            val mentor = Mentor(
                jsonObject.getString("name"),
                jsonObject.getString("price"),
                jsonObject.getString("role"),
                jsonObject.getString("status"),
                dpUrlWithTimestamp,  // to avoid cache issues
                jsonObject.getString("category"),
                jsonObject.getString("description"),
                jsonObject.getString("email"),
                email.toString()
                //jsonObject.getString("video")
            )
            mentors.add(mentor)
        }


        return mentors
    }
}