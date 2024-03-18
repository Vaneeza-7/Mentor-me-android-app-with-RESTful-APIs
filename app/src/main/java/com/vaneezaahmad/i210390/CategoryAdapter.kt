package com.vaneezaahmad.i210390

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(private val categories: List<Category>, private val listener: OnItemClickListener) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(category: Category)
    }

    var selectedPosition = -1

    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.text_category)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.name.text = category.name
        holder.itemView.setOnClickListener {
            selectedPosition = position
            listener.onItemClick(category)
            notifyDataSetChanged()
        }

        if (position == selectedPosition) {
            holder.name.setTextColor(Color.parseColor("#FFFFFF"))
            holder.itemView.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#157177"))
        } else {
            holder.name.setTextColor(Color.parseColor("#157177"))
            holder.itemView.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#8ED9DD"))
        }
    }

    override fun getItemCount() = categories.size
}