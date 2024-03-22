package com.vaneezaahmad.i210390

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Chat_fragment2 : Fragment(R.layout.fragment2_chat){

    var mAuth = FirebaseAuth.getInstance()
    var database = FirebaseDatabase.getInstance()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userRef = database.getReference("users")
        val mentorUid = mAuth.currentUser?.uid
        val messageRef = database.getReference("messages")
        val chats =  mutableListOf<User>();
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        messageRef.get().addOnSuccessListener { dataSnapshot ->
            for (messageSnapshot in dataSnapshot.children) {
                val message = messageSnapshot.getValue(Message::class.java)
                if (message != null) {
                    if (message.sender == mentorUid) {
                        // The receiver is the other user
                        userRef.child(message.receiver).get().addOnSuccessListener { userSnapshot ->
                            val user = userSnapshot.getValue(User::class.java)
                            if (user != null && user !in chats) {
                                chats.add(user)
                                // Set the adapter here after the user is added to the chats list
                                recyclerView.adapter = UserAdapter(chats)
                                recyclerView.adapter?.notifyDataSetChanged()
                            }
                        }
                    } else if (message.receiver == mentorUid) {
                        // The sender is the other user
                        userRef.child(message.sender).get().addOnSuccessListener { userSnapshot ->
                            val user = userSnapshot.getValue(User::class.java)
                            if (user != null && user !in chats) {
                                chats.add(user)
                                // Set the adapter here after the user is added to the chats list
                                recyclerView.adapter = UserAdapter(chats)
                                recyclerView.adapter?.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }
        }
    }
}