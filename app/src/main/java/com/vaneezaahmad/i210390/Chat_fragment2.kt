package com.vaneezaahmad.i210390

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class Chat_fragment2 : Fragment(R.layout.fragment2_chat){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chats =  mutableListOf<User>();
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

    }
}