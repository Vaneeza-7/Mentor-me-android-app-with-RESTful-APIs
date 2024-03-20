package com.vaneezaahmad.i210390

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide


class SearchListAdapter(private var mentors: List<Mentor>) : RecyclerView.Adapter<SearchListAdapter.SearchListViewHolder>() {

    class SearchListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.name)
        val image = view.findViewById<ImageView>(R.id.image)
        val row = view.findViewById<LinearLayout>(R.id.linearLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_listmentor, parent, false)
        return SearchListViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchListViewHolder, position: Int) {
        val mentor = mentors[position]
        holder.name.text = mentor.name
        Glide.with(holder.image.context).load(mentor.image).into(holder.image)
        holder.row.setOnClickListener {
            val intent = Intent(holder.row.context, Activity8::class.java)
            intent.putExtra("mentorName", mentor.name)
            intent.putExtra("mentorRole", mentor.role)
            intent.putExtra("mentorDescription", mentor.description)
            intent.putExtra("mentorImage", mentor.image)
            intent.putExtra("mentorEmail", mentor.email)
            intent.putExtra("mentorPrice", mentor.price)
            holder.row.context.startActivity(intent)

        }
    }

    override fun getItemCount() = mentors.size

    fun filterList(filteredList: List<Mentor>) {
        mentors = filteredList
        notifyDataSetChanged()
    }
}