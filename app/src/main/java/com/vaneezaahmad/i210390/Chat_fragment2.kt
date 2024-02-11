package com.vaneezaahmad.i210390

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class Chat_fragment2 : Fragment(R.layout.fragment2_chat){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val back = view.findViewById<ImageButton>(R.id.back)

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
    }
}