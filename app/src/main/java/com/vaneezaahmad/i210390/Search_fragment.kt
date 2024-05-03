package com.vaneezaahmad.i210390


import android.R.layout
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray


class Search_fragment : Fragment(R.layout.fragment_search){

    private lateinit var adapter: SearchListAdapter
    private var mentors = ArrayList<Mentor>()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val email = arguments?.getString("email").toString()


        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = SearchListAdapter(mentors)
        recyclerView.adapter = adapter
        fetchMentors(getString(R.string.IP) + "mentorme/getMentors.php", email)

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
            val fragment = second_search_fragment()
            fragment.arguments = Bundle().apply {
                putString("email", email)
            }
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container_view, fragment)
            fragmentTransaction.addToBackStack(null) // Optional: Add transaction to back stack
            fragmentTransaction.commit()
        }

        val back = view.findViewById<ImageView>(R.id.arrow)

        back.setOnClickListener {
            // Replace the current fragment with the new fragment
//            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
//            fragmentTransaction.replace(R.id.fragment_container_view, Home_fragment())
//            fragmentTransaction.addToBackStack(null) // Optional: Add transaction to back stack
//            fragmentTransaction.commit()

            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    fun fetchMentors(url: String, email: String?) {
        val requestQueue = Volley.newRequestQueue(context)
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val mentors = parseMentors(response, email.toString())
                this.mentors = mentors as ArrayList<Mentor>
                adapter = SearchListAdapter(mentors)
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