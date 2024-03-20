package com.vaneezaahmad.i210390

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import de.hdodenhof.circleimageview.CircleImageView

class Chat_fragment : Fragment(R.layout.fragment_chat){
    var mAuth = FirebaseAuth.getInstance()
    var database = FirebaseDatabase.getInstance()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val back = view.findViewById<ImageView>(R.id.backButton)

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
            fragmentTransaction.replace(R.id.fragment_container_view, Chat_fragment2())
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

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val chats = mutableListOf<Mentor>()
        val mentorsRef = database.getReference("Mentors")
        mentorsRef.get().addOnSuccessListener {
            for (mentor in it.children) {
                val mentorObj = mentor.getValue(Mentor::class.java)
                if (mentorObj != null) {
                    chats.add(mentorObj)
                }
            }
            recyclerView.adapter = ChatAdapter(chats)
        }


    }
}