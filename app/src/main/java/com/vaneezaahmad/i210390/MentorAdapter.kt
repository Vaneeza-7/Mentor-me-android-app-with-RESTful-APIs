package com.vaneezaahmad.i210390

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView


class MentorAdapter(private var mentors: List<Mentor>) : RecyclerView.Adapter<MentorAdapter.MentorViewHolder>() {
    private val favoriteMentorIds = mutableSetOf<String>()
    private val mentorUids = mutableMapOf<Mentor, String>()

    init {
    }
    init {
    }

    class MentorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.text_name)
        val price: TextView = view.findViewById(R.id.text_price)
        val role: TextView = view.findViewById(R.id.text_role)
        val status: TextView = view.findViewById(R.id.text_status)
        val image: ImageView = view.findViewById(R.id.image_mentor)
        var card = view.findViewById<MaterialCardView>(R.id.card)
        var favorite = view.findViewById<ImageView>(R.id.image_favorite)
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
        Glide.with(holder.image.context)
            .load(mentor.image)
            .into(holder.image)

        val mentorUid = mentorUids[mentor]
        val isFavorite = favoriteMentorIds.contains(mentorUid)
        holder.favorite.setImageResource(if (isFavorite) R.drawable.baseline_favorite_24 else R.drawable.unfavorite)

        holder.favorite.setOnClickListener {
            if (isFavorite) {
               // favoritesRef.child(mentorUid!!).removeValue()
            } else {
                //favoritesRef.child(mentorUid!!).setValue(true)
            }
        }

        holder.card.setOnClickListener {
            val intent = Intent(holder.card.context, Activity8::class.java)
            intent.putExtra("mentorName", mentor.name)
            intent.putExtra("mentorRole", mentor.role)
            intent.putExtra("mentorDescription", mentor.description)
            intent.putExtra("mentorImage", mentor.image)
            intent.putExtra("mentorEmail", mentor.email)
            intent.putExtra("mentorPrice", mentor.price)
            intent.putExtra("videoDisguised", mentor.video)
            holder.card.context.startActivity(intent)
        }
    }

    override fun getItemCount() = mentors.size

    fun updateMentors(newMentors: List<Mentor>) {
        this.mentors = newMentors
        notifyDataSetChanged()
    }
}