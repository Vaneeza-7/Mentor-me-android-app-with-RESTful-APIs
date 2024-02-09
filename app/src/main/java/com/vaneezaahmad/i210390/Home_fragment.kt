package com.vaneezaahmad.i210390

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment


class Home_fragment : Fragment(R.layout.fragment_home){
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

        view.findViewById<CardView>(R.id.card).setOnClickListener {
            val intent = Intent(requireContext(), Activity8::class.java)
            startActivity(intent)
        }
    }
}