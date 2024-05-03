package com.vaneezaahmad.i210390

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
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener


class MentorHomeFragment : Fragment(R.layout.mentorhome_fragment), CategoryAdapter.OnItemClickListener {

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

        val email = arguments?.getString("email")

        //fetch user name here
        var url = getString(R.string.IP) + "mentorme/getMentorData.php"
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
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
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

        //mentor recycler view
        mentors = ArrayList<Mentor>();
        mentors.add(Mentor("John Doe", "100", "Teacher", "Active", "android.resource://com.vaneezaahmad.i210390/drawable/${R.drawable.john_cooper}", "Education", "Experienced teacher", "john.doe@example.com", "video_url"))
        mentors.add(Mentor("Jane Smith", "120", "Engineer", "Active","android.resource://com.vaneezaahmad.i210390/drawable/${R.drawable.drake}" , "Technology", "Experienced engineer", "jane.smith@example.com", "video_url"))
        mentors.add(Mentor("Bob Johnson", "150", "Entrepreneur", "Active", "android.resource://com.vaneezaahmad.i210390/drawable/${R.drawable.john_cooper}", "Entrepreneurship", "Successful entrepreneur", "bob.johnson@example.com", "video_url"))
        mentors.add(Mentor("Alice Williams", "80", "Chef", "Active", "android.resource://com.vaneezaahmad.i210390/drawable/${R.drawable.john_cooper}", "Cooking", "Professional chef", "alice.williams@example.com", "video_url"))
        mentors.add(Mentor("Charlie Brown", "90", "Coach", "Active", "android.resource://com.vaneezaahmad.i210390/drawable/${R.drawable.john_cooper}", "Personal Growth", "Life coach", "charlie.brown@example.com", "video_url"));

        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = MentorAdapter(mentors)

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
}