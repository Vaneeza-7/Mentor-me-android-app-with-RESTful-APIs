package com.vaneezaahmad.i210390

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener


class Home_fragment : Fragment(R.layout.fragment_home), CategoryAdapter.OnItemClickListener {

    private lateinit var mentors: ArrayList<Mentor>
    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.bell).setOnClickListener {
            val intent = Intent(requireContext(), NotificationsActivity::class.java)
            startActivity(intent)
        }

        //val sharedPreferences = requireContext().getSharedPreferences("MySharedPref", MODE_PRIVATE)
        //val editor = sharedPreferences.edit()

        val email = arguments?.getString("email").toString()

//        if (email != null) {
//            editor.putString("email", email)
//            editor.apply()
//            Toast.makeText(context, email, Toast.LENGTH_SHORT).show()
//        } else {
//
//            email = sharedPreferences.getString("email", null)
//            if (email == null) {
//                Toast.makeText(context, "Email not available", Toast.LENGTH_SHORT).show()
//                return
//            }
//        }


        var url = getString(R.string.IP) + "mentorme/getUserData.php"
        val request = object : StringRequest(
            Method.POST, url,
            { response ->
                val jsonResponse = JSONObject(response)
                if (jsonResponse.has("name")) {
                    val name = jsonResponse.getString("name")
                    if(name == "null") {
                        view.findViewById<TextView>(R.id.text_name).text = "User"
                    }
                    else {
                        view.findViewById<TextView>(R.id.text_name).text = name
                    }

                } else {

                    Toast.makeText(context, "No name in JsonObject", Toast.LENGTH_SHORT).show()
                }

            },
            { error ->
                Toast.makeText(context, "error.message", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val map = HashMap<String, String>()
                map["email"] = email.toString()
                return map
            }
        }
        val queue = Volley.newRequestQueue(context)
        queue.add(request)


        //category recycler view
        val categories = ArrayList<Category>()
        categories.add(Category("All"))
        categories.add(Category("Education"))
        categories.add(Category("Technology"))
        categories.add(Category("Entrepreneurship"))
        categories.add(Category("Personal Growth"))
        categories.add(Category("Career Development"))
        categories.add(Category("Cooking"))

        val categoriesRecyclerView = view.findViewById<RecyclerView>(R.id.categoriesRecyclerView)
        categoriesRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        categoriesRecyclerView.adapter = CategoryAdapter(categories, this)


        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        fetchMentors(getString(R.string.IP) + "mentorme/getMentors.php", email)
    }
    fun filterMentors(category: String) {
        val filteredMentors = if (category == "All") {
            mentors
        } else {
            mentors.filter { it.category == category }
        }

        (recyclerView.adapter as MentorAdapter).updateMentors(filteredMentors)
    }
    override fun onItemClick(category: Category) {
        filterMentors(category.name)
    }

    fun fetchMentors(url: String, email: String?) {
        val requestQueue = Volley.newRequestQueue(context)
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val mentors = parseMentors(response, email.toString())
                this.mentors = mentors as ArrayList<Mentor>
                recyclerView.adapter = MentorAdapter(mentors)
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