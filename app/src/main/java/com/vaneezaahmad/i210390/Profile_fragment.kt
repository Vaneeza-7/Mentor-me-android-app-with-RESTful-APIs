package com.vaneezaahmad.i210390

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONObject

class Profile_fragment : Fragment(R.layout.fragment_profile){
    //private lateinit var selectImageLauncher: ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val email = arguments?.getString("email")

        var url = getString(R.string.IP) + "mentorme/getUserData.php"
        val request = object : StringRequest(
            Method.POST, url,
            { response ->
                val jsonResponse = JSONObject(response)
                if (jsonResponse.has("name") && jsonResponse.has("city")) {
                    val name = jsonResponse.getString("name")
                    val city = jsonResponse.getString("city")
                    if(name == "null") {
                        view.findViewById<TextView>(R.id.name).text = "User"
                    }
                    else {
                        view.findViewById<TextView>(R.id.name).text = name
                        view.findViewById<TextView>(R.id.location) .text = city
                    }

                } else {

                    Toast.makeText(context, "Not complete JsonObject", Toast.LENGTH_SHORT).show()
                }

            },
            { error ->
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val map = HashMap<String, String>()
                map["email"] = email.toString()
                return map
            }
        }
        val queue = Volley.newRequestQueue(context)
        queue.add(request)


        view.findViewById<ImageView>(R.id.backButton).setOnClickListener {
            val intent = Intent(requireContext(), Activity7::class.java)
            startActivity(intent)
        }

        view.findViewById<ImageButton>(R.id.more).setOnClickListener {
            val intent = Intent(requireContext(), Activity16::class.java)
            intent.putExtra("email", email  )
            startActivity(intent)
        }

        view.findViewById<Button>(R.id.bookedSessions).setOnClickListener {
            val intent = Intent(requireContext(), Activity17::class.java)
            startActivity(intent)
        }

        view.findViewById<ImageButton>(R.id.logout).setOnClickListener {

            //mAuth.signOut()
            Toast.makeText(requireContext(), "Logged Out", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), ActivityChoose::class.java)
            startActivity(intent)
            activity?.finish()
        }

        view.findViewById<Button>(R.id.iconButton).setOnClickListener {

        //    selectImageLauncher.launch("image/*")
            /*val intent = Intent(requireContext(), Activity12::class.java)
            startActivity(intent)*/

            //selectAndUploadImage(view, "images", R.drawable.profile_modified)
        }

        view.findViewById<Button>(R.id.iconButton2).setOnClickListener {
          //  selectCoverLauncher.launch("image/*")
            //selectAndUploadImage(view, "coverImages", R.drawable.editprofile)
        }

        val mentors = ArrayList<Mentor>()
        val recyclerView = view.findViewById<RecyclerView>(R.id.favoriteMentorsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = MentorAdapter(mentors)

        //review recycler view
        val reviews = ArrayList<Review>()
        val review_recyclerView = view.findViewById<RecyclerView>(R.id.myReviewsRecyclerView)

        review_recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        review_recyclerView.adapter = ReviewAdapter(reviews)
    }

}