package com.vaneezaahmad.i210390

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONArray

class Chat_fragment : Fragment(R.layout.fragment_chat){

    private lateinit var recyclerView: RecyclerView
    private lateinit var chats: ArrayList<Mentor>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val back = view.findViewById<ImageView>(R.id.backButton)
        val email = arguments?.getString("email").toString()

        back.setOnClickListener {
            // Replace the current fragment with the new fragment
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container_view, Home_fragment())
            fragmentTransaction.addToBackStack(null) // Optional: Add transaction to back stack
            fragmentTransaction.commit()
        }

/*
        val openChat = view.findViewById<TextView>(R.id.msg1)
        openChat.setOnClickListener {
            // Replace the current fragment with the new fragment
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container_view, MentorChatFragment())
            fragmentTransaction.addToBackStack(null) // Optional: Add transaction to back stack
            fragmentTransaction.commit()
        }
*/

        val openCommunity = view.findViewById<CircleImageView>(R.id.status1)
        openCommunity.setOnClickListener {
            // Replace the current fragment with the new fragment
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container_view, Comunity_fragment())
            fragmentTransaction.addToBackStack(null) // Optional: Add transaction to back stack
            fragmentTransaction.commit()
        }

        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        fetchMentors(getString(R.string.IP) + "mentorme/getMentors.php", email)


    }

    fun fetchMentors(url: String, email: String?) {
        val requestQueue = Volley.newRequestQueue(context)
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val mentors = parseMentors(response, email.toString())
                this.chats = mentors as ArrayList<Mentor>
                recyclerView.adapter = ChatAdapter(mentors)
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