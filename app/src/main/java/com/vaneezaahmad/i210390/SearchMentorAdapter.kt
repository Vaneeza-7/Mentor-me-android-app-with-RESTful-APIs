package com.vaneezaahmad.i210390

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class SearchMentorAdapter(private val items: List<Mentor>) : RecyclerView.Adapter<SearchMentorAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.text_name)
        val price: TextView = view.findViewById(R.id.text_price)
        val role : TextView = view.findViewById(R.id.text_role)
        val status : TextView = view.findViewById(R.id.text_status)
        val image : ImageView = view.findViewById(R.id.image_mentor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_searchmentor, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.name.text = item.name
        holder.price.text = item.price
        holder.role.text = item.role
        holder.status.text = item.status
        Glide.with(holder.image.context)
            .load(item.image.toUri())
            .into(holder.image)
    }

    override fun getItemCount() = items.size
}