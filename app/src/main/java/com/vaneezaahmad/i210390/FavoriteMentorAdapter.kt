package com.vaneezaahmad.i210390

import android.content.Intent
import android.view.LayoutInflater
import android.R.layout
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import org.json.JSONObject


class FavoriteMentorAdapter(private var mentors: List<Mentor>) : RecyclerView.Adapter<FavoriteMentorAdapter.MentorViewHolder>() {
    var favoriteMentors = mutableListOf<String>()
    private var filteredMentors = mentors.filter { mentor -> favoriteMentors.contains(mentor.email) }
    var count = 0;
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

        if(count==0)
        {
            fetchFavoriteMentors(holder.favorite.context, mentor.video)
            count++
        }

        holder.favorite.setImageResource(if (favoriteMentors.contains(mentor.email))
            R.drawable.baseline_favorite_24 else R.drawable.unfavorite)

        holder.favorite.setOnClickListener {
            val isCurrentlyFavorite = favoriteMentors.contains(mentor.email)
            updateFavorite(mentor.video, mentor.email ,holder.favorite.context, !isCurrentlyFavorite)
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
        filteredMentors = newMentors.filter { mentor -> favoriteMentors.contains(mentor.email) }
        notifyDataSetChanged()
    }

    fun fetchFavoriteMentors(context: Context, userEmail: String) {
        val url = context.getString(R.string.IP) + "mentorme/getfavEmails.php"
        val requestQueue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(Method.POST, url,
            Response.Listener<String> { response ->
                try {
                    val jsonResponse = JSONObject(response)
                    if (jsonResponse.getInt("status") == 1) {
                        val mentorsArray = jsonResponse.getJSONArray("mentors")
                        favoriteMentors.clear()
                        for (i in 0 until mentorsArray.length()) {
                            favoriteMentors.add(mentorsArray.getString(i))
                        }
                        updateMentors(mentors.filter { mentor -> favoriteMentors.contains(mentor.email) })
                       // Toast.makeText(context, "Favorites updated", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to fetch favorites", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Error parsing the response", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(context, error.message ?: "Network error", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["useremail"] = userEmail
                return params
            }
        }
        requestQueue.add(stringRequest)
    }

    fun updateFilteredMentors() {
        filteredMentors = mentors.filter { mentor -> favoriteMentors.contains(mentor.email) }
        notifyDataSetChanged()
    }

    private fun updateFavorite(userEmail: String, mentorEmail:String, context: Context, add: Boolean) {
        val url = context.getString(R.string.IP) + (if (add) "mentorme/addfavorite.php" else "mentorme/deletefavorite.php")

        val requestQueue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(Method.POST, url,
            Response.Listener<String> { response ->
                val jsonResponse = JSONObject(response)
                if (jsonResponse.getInt("status") == 1) {
                    if (add) {
                        favoriteMentors.add(mentorEmail)
                        Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show()
                    } else {
                        favoriteMentors.remove(mentorEmail)
                        Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show()
                    }
                    notifyDataSetChanged()
                }
                else {
                    Toast.makeText(context, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show()
                    Toast.makeText(context, "Failed to update favorite status", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(context, "error.message", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["useremail"] = userEmail
                params["mentoremail"] = mentorEmail
                return params
            }
        }
        requestQueue.add(stringRequest)
    }

}