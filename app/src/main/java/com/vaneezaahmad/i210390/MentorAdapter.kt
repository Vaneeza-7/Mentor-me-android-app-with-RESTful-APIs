package com.vaneezaahmad.i210390

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MentorAdapter(private var mentors: List<Mentor>) : RecyclerView.Adapter<MentorAdapter.MentorViewHolder>() {

    class MentorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.text_name)
        val price: TextView = view.findViewById(R.id.text_price)
        val role: TextView = view.findViewById(R.id.text_role)
        val status: TextView = view.findViewById(R.id.text_status)
        val image: ImageView = view.findViewById(R.id.image_mentor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MentorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mentor, parent, false)
        return MentorViewHolder(view)
    }

    override fun onBindViewHolder(holder: MentorViewHolder, position: Int) {
        val mentor = mentors[position]
        holder.name.text = mentor.name
        holder.price.text = mentor.price
        holder.role.text = mentor.role
        holder.status.text = mentor.status
        holder.image.setImageResource(mentor.imageResource)
    }

    override fun getItemCount() = mentors.size

    fun updateMentors(newMentors: List<Mentor>) {
        this.mentors = newMentors
        notifyDataSetChanged()
    }
}