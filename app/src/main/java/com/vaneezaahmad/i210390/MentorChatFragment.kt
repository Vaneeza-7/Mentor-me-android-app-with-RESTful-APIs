package com.vaneezaahmad.i210390

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONArray
import org.json.JSONException

class MentorChatFragment : Fragment(R.layout.mentorchat_fragment) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var chats: ArrayList<User>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val back = view.findViewById<ImageView>(R.id.backButton)
        val email = arguments?.getString("email").toString()

        back.setOnClickListener {
            // Replace the current fragment with the new fragment
//            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
//            fragmentTransaction.replace(R.id.fragment_container_view, Home_fragment())
//            fragmentTransaction.addToBackStack(null) // Optional: Add transaction to back stack
//            fragmentTransaction.commit()

            //popbackstack
            requireActivity().supportFragmentManager.popBackStack()
        }

        val openCommunity = view.findViewById<CircleImageView>(R.id.status1)
        openCommunity.setOnClickListener {
            // Replace the current fragment with the new fragment
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container_view, Comunity_fragment())
            fragmentTransaction.addToBackStack(null) // Optional: Add transaction to back stack
            fragmentTransaction.commit()
        }

        chats = ArrayList<User>();
        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = UserAdapter(chats)
        fetchUsers(getString(R.string.IP) + "mentorme/getAllUsers.php", email)


    }

    fun fetchUsers(url: String, email : String) {
        val requestQueue = Volley.newRequestQueue(context)

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    if (response.getInt("status") == 1) {
                        val usersArray = response.getJSONArray("users")
                        for (i in 0 until usersArray.length()) {
                            val user = usersArray.getJSONObject(i)
                            chats.add(
                                User(
                                    user.getString("name"),
                                    user.getString("email"),
                                    user.getString("phone"),
                                    user.getString("city"),
                                    email,
                                    user.getString("dp")
                                )
                            )
                        }
                        recyclerView.adapter?.notifyDataSetChanged()
                    } else {
                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    Toast.makeText(context, "Error parsing the JSON response", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )

        requestQueue.add(request)
    }


}