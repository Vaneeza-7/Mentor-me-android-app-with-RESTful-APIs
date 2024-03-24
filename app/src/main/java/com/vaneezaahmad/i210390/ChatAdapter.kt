package com.vaneezaahmad.i210390

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import de.hdodenhof.circleimageview.CircleImageView

class ChatAdapter (val chats: List<Mentor>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    private val database = FirebaseDatabase.getInstance()
    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.username)
        val profileImage = view.findViewById<CircleImageView>(R.id.profile_image)
        val relativeLayout = view.findViewById<View>(R.id.RelativeLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chats[position]
        holder.name.text = chat.name
        Glide.with(holder.profileImage.context).load(chat.image).into(holder.profileImage)
        var mentorUid : String = ""
        val mentorRef = database.getReference("Mentors")
        mentorRef.get().addOnSuccessListener {
            for (mentor in it.children) {
                val mentorObj = mentor.getValue(Mentor::class.java)
                if (mentorObj != null) {
                    if (mentorObj.email == chat.email) {
                        mentorUid = mentor.key.toString()
                    }
                }
            }
        }
        holder.relativeLayout.setOnClickListener {
            val intent = Intent(holder.relativeLayout.context, MessageActivity::class.java)
            intent.putExtra("mentorName", chat.name)
            intent.putExtra("mentorEmail", chat.email)
            intent.putExtra("mentorImage", chat.image)
            intent.putExtra("mentorUid", mentorUid)
            holder.relativeLayout.context.startActivity(intent)
        }
    }

    override fun getItemCount() = chats.size
}