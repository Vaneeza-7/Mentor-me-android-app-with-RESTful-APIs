package com.vaneezaahmad.i210390

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
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
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class Profile_fragment : Fragment(R.layout.fragment_profile){
    //private lateinit var selectImageLauncher: ActivityResultLauncher<String>
    private var imageString: String? = null
    private var imageStringCp: String? = null
    var reviews = ArrayList<Review>()
    private lateinit var review_recyclerView: RecyclerView
    private lateinit var mentors: ArrayList<Mentor>
    private lateinit var recyclerView: RecyclerView
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
                if (jsonResponse.has("name") && jsonResponse.has("city") && jsonResponse.has("dp") && jsonResponse.has("cp")) {
                    val name = jsonResponse.getString("name")
                    val city = jsonResponse.getString("city")
                    val dp = jsonResponse.getString("dp")
                    val cp = jsonResponse.getString("cp")
                    if(name == "null" || city == "null" ) {
                        view.findViewById<TextView>(R.id.name).text = "User"
                        view.findViewById<TextView>(R.id.location) .text = "Dhok Mandaal"
                    }
                    else {
                        view.findViewById<TextView>(R.id.name).text = name
                        view.findViewById<TextView>(R.id.location) .text = city
                    }
                    if (dp == "null" || dp == "") {
                        Glide.with(this).load(R.drawable.profile_modified).into(view.findViewById<CircleImageView>(R.id.profileImage))
                    }
                    else {
                        val timestamp = System.currentTimeMillis()
                        Glide.with(this).load("$dp?timestamp=$timestamp").into(view.findViewById<CircleImageView>(R.id.profileImage))
                    }
                    if (cp == "null" || cp == "") {
                        Glide.with(this).load(R.drawable.editprofile).into(view.findViewById<ImageView>(R.id.editProfile))
                    }
                    else {
                        val timestamp = System.currentTimeMillis()
                        Glide.with(this).load("$cp?timestamp=$timestamp").into(view.findViewById<ImageView>(R.id.editProfile))
                    }

                } else {

                    Toast.makeText(context, "Not complete JsonObject", Toast.LENGTH_SHORT).show()
                }

            },
            { error ->
                Toast.makeText(context, "its "+ email.toString(), Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "error.message", Toast.LENGTH_SHORT).show()
                //Toast.makeText(context, email.toString(), Toast.LENGTH_SHORT).show()
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

        val selectImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val bitmap = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                    MediaStore.Images.Media.getBitmap(requireContext().contentResolver, it)
                } else {
                    val source = ImageDecoder.createSource(requireContext().contentResolver, it)
                    ImageDecoder.decodeBitmap(source)
                }
                val imageView = view.findViewById<CircleImageView>(R.id.profileImage)
                imageView.setImageBitmap(bitmap)

                // Convert the bitmap to Base64 string
                imageString = bitmapToBase64(bitmap)
                uploadImage(imageString!!, email!!, "DP")
            }
        }

        val selectCoverLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val bitmap = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                    MediaStore.Images.Media.getBitmap(requireContext().contentResolver, it)
                } else {
                    val source = ImageDecoder.createSource(requireContext().contentResolver, it)
                    ImageDecoder.decodeBitmap(source)
                }
                val imageView = view.findViewById<ImageView>(R.id.editProfile)
                imageView.setImageBitmap(bitmap)

                // Convert the bitmap to Base64 string
                imageStringCp = bitmapToBase64(bitmap)
                uploadImage(imageStringCp!!, email!!, "Cover")
            }
        }

        view.findViewById<Button>(R.id.iconButton).setOnClickListener {

             selectImageLauncher.launch("image/*")

            //selectAndUploadImage(view, "images", R.drawable.profile_modified)
        }

        view.findViewById<Button>(R.id.iconButton2).setOnClickListener {
            selectCoverLauncher.launch("image/*")
            //selectAndUploadImage(view, "coverImages", R.drawable.editprofile)
        }

        recyclerView = view.findViewById<RecyclerView>(R.id.favoriteMentorsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        fetchMentors(getString(R.string.IP) + "mentorme/getFavoriteMentors.php", email)

        //review recycler view

        review_recyclerView = view.findViewById<RecyclerView>(R.id.myReviewsRecyclerView)
        review_recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        fetchReviews(getString(R.string.IP) + "mentorme/getReviews.php", email)
    }

    fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun uploadImage(image: String, email: String, type:String) {
        var url = "";
        if(type == "Cover")
        {
            url = getString(R.string.IP) + "mentorme/uploadCover.php"
        }
        else
        {
            url = getString(R.string.IP) + "mentorme/uploaddp.php"
        }
        val requestQueue = Volley.newRequestQueue(requireContext())

        val imageRequest = object : StringRequest(Method.POST, url,
            { response ->
                val jsonResponse = JSONObject(response)
                val status = jsonResponse.getInt("status")
                val message = jsonResponse.getString("message")
                if(status == 1) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Log.d("ImageError", error.toString())
                Toast.makeText(context, "An error occured", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["image"] = image
                params["email"] = email
                params["localhost"] = getString(R.string.IP)
                return params
            }
        }
        requestQueue.add(imageRequest)
    }

    fun fetchReviews(url: String, email: String?) {
        val requestQueue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(
            Method.POST, url,
            { response ->
                val jsonObject = JSONObject(response)
                val jsonArray = jsonObject.getJSONArray("reviews")
                val reviews = parseReviews(jsonArray, email.toString())
                this.reviews = reviews as ArrayList<Review>
                review_recyclerView.adapter = ReviewAdapter(reviews)
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(context, "Could not fetch reviews", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val map = HashMap<String, String>()
                map["useremail"] = email.toString()
                return map
            }
        }

        requestQueue.add(stringRequest)
    }

    fun parseReviews(jsonArray: JSONArray, email: String?): List<Review> {
        val reviews = mutableListOf<Review>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val review = Review(
                jsonObject.getString("mentorname"),
                jsonObject.getDouble("rating").toFloat(),
                jsonObject.getString("text")
            )
            reviews.add(review)
        }

        return reviews
    }

    fun fetchMentors(url: String, email: String?) {
        val requestQueue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(
            Method.POST, url,
            { response ->
                val jsonObject = JSONObject(response)
                val jsonArray = jsonObject.getJSONArray("mentors")
                val mentors = parseMentors(jsonArray, email.toString())
                this.mentors = mentors as ArrayList<Mentor>
                recyclerView.adapter = MentorAdapter(mentors)
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(context, "Could not fetch mentors", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val map = HashMap<String, String>()
                map["useremail"] = email.toString()
                return map
            }
        }

        requestQueue.add(stringRequest)
    }

    fun parseMentors(jsonArray: JSONArray, email: String?): List<Mentor> {
        val mentors = mutableListOf<Mentor>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val dpUrl = jsonObject.getString("dp")
            val timestamp = System.currentTimeMillis()
            val dpUrlWithTimestamp = if (dpUrl.isNotEmpty()) "$dpUrl?timestamp=$timestamp" else dpUrl

            val mentor = Mentor(
                jsonObject.getString("name"),
                jsonObject.getString("price"),
                jsonObject.getString("role"),
                jsonObject.getString("status"),
                dpUrlWithTimestamp,  // to avoid cache issues
                jsonObject.getString("category"),
                jsonObject.getString("description"),
                jsonObject.getString("email"),
                email.toString()
                //jsonObject.getString("video")
            )
            mentors.add(mentor)
        }


        return mentors
    }
}