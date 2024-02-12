package com.vaneezaahmad.i210390

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

class Profile_fragment : Fragment(R.layout.fragment_profile){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<CardView>(R.id.card).setOnClickListener {
            val intent = Intent(requireContext(), Activity8::class.java)
            startActivity(intent)
        }


        view.findViewById<ImageView>(R.id.backButton).setOnClickListener {
            val intent = Intent(requireContext(), Activity7::class.java)
            startActivity(intent)
        }

        view.findViewById<Button>(R.id.iconButton).setOnClickListener {
            val intent = Intent(requireContext(), Activity12::class.java)
            startActivity(intent)
        }

        view.findViewById<Button>(R.id.iconButton2).setOnClickListener {
            val intent = Intent(requireContext(), Activity12::class.java)
            startActivity(intent)
        }

        view.findViewById<ImageButton>(R.id.more).setOnClickListener {
            val intent = Intent(requireContext(), Activity16::class.java)
            startActivity(intent)
        }

        view.findViewById<Button>(R.id.bookedSessions).setOnClickListener {
            val intent = Intent(requireContext(), Activity17::class.java)
            startActivity(intent)
        }

    }
}