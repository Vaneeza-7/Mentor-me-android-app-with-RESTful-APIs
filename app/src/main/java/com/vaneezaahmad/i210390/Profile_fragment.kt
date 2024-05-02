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
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class Profile_fragment : Fragment(R.layout.fragment_profile){
    //private lateinit var selectImageLauncher: ActivityResultLauncher<String>
    private var imageString: String? = null
    private var imageStringCp: String? = null
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
                        Glide.with(this).load(dp).into(view.findViewById<CircleImageView>(R.id.profileImage))
                    }
                    if (cp == "null" || cp == "") {
                        Glide.with(this).load(R.drawable.editprofile).into(view.findViewById<ImageView>(R.id.editProfile))
                    }
                    else {
                        Glide.with(this).load(cp).into(view.findViewById<ImageView>(R.id.editProfile))
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


}