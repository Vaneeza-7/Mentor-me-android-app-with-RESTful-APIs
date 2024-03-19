package com.vaneezaahmad.i210390

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


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

        //view.findViewById<CardView>(R.id.card).setOnClickListener {
          //  val intent = Intent(requireContext(), Activity8::class.java)
           // startActivity(intent)
        //}

        var uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val firebaseRef = FirebaseDatabase.getInstance().getReference("users").child(uid.toString())
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Get the username
                    val username = dataSnapshot.child("name").value.toString()
                    view.findViewById<TextView>(R.id.text_name).text = username
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

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
        mentors = ArrayList<Mentor>()
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
                    recyclerView.adapter?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

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