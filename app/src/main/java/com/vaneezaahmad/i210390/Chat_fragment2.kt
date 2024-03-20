package com.vaneezaahmad.i210390

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Chat_fragment2 : Fragment(R.layout.fragment2_chat){

    var mAuth = FirebaseAuth.getInstance()
    var database = FirebaseDatabase.getInstance()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       // val mentor = arguments?.getParcelable<Mentor>("mentor")
       /* val back = view.findViewById<ImageButton>(R.id.back)

        back.setOnClickListener {
            // Replace the current fragment with the new fragment
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container_view, Chat_fragment())
            fragmentTransaction.addToBackStack(null) // Optional: Add transaction to back stack
            fragmentTransaction.commit()
        }
        view.findViewById<ImageButton>(R.id.camera).setOnClickListener {
            val intent = Intent(requireContext(), Activity12::class.java)
            startActivity(intent)
        }

        view.findViewById<ImageButton>(R.id.video).setOnClickListener {
            val intent = Intent(requireContext(), Activity14::class.java)
            startActivity(intent)
        }

        view.findViewById<ImageButton>(R.id.call).setOnClickListener {
            val intent = Intent(requireContext(), Activity15::class.java)
            startActivity(intent)
        }

        val sendButton = view.findViewById<ImageButton>(R.id.send)
        val messageBox = view.findViewById<TextInputEditText>(R.id.message_box)

        sendButton.setOnClickListener {
            val messageText = messageBox.text.toString()
            if (messageText.isNotEmpty()) {

                val sender = mAuth.currentUser?.uid
                val receiver = "receiverId"
                val timestamp = System.currentTimeMillis()
                val read = false
                val senderImage = "senderImage"

                val message = sender?.let { it1 ->
                    Message(messageText,
                        it1, receiver, timestamp, read, senderImage)
                }

                val messageRef = database.getReference("messages")
                messageRef.push().setValue(message)

                // Clear the message box for the next message
                messageBox.text?.clear()
            }
        }*/
    }
}