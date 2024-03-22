package com.vaneezaahmad.i210390

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(val users: MutableList<User>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.username)
        val profileImage = view.findViewById<CircleImageView>(R.id.profile_image)
        val relativeLayout = view.findViewById<View>(R.id.RelativeLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user: User = users[position]
        holder.name.text = user.name
        Glide.with(holder.profileImage.context).load(user.image).into(holder.profileImage)
        holder.relativeLayout.setOnClickListener {
            val intent = Intent(holder.relativeLayout.context, MessageActivity::class.java)
            intent.putExtra("userName", user.name)
            intent.putExtra("userEmail", user.email)
            intent.putExtra("userImage", user.image)
            holder.relativeLayout.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }


}