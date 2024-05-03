package com.vaneezaahmad.i210390

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class MentorChatFragment : Fragment(R.layout.mentorchat_fragment){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chats =  mutableListOf<User>();
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        //val adapter = ChatAdapter(chats)
    }
}