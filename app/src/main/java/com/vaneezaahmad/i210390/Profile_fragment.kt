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
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView

class Profile_fragment : Fragment(R.layout.fragment_profile){
    var mAuth = FirebaseAuth.getInstance();
    var uid = mAuth.currentUser?.uid.toString()
    val firebaseRef = FirebaseDatabase.getInstance().getReference("users").child(uid.toString())
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



        //view.findViewById<CardView>(R.id.card).setOnClickListener {
          //  val intent = Intent(requireContext(), Activity8::class.java)
            //startActivity(intent)
        //}


        view.findViewById<ImageView>(R.id.backButton).setOnClickListener {
            val intent = Intent(requireContext(), Activity7::class.java)
            startActivity(intent)
        }

        view.findViewById<ImageButton>(R.id.more).setOnClickListener {
            val intent = Intent(requireContext(), Activity16::class.java)
            startActivity(intent)
        }

        view.findViewById<Button>(R.id.bookedSessions).setOnClickListener {
            val intent = Intent(requireContext(), Activity17::class.java)
            startActivity(intent)
        }

        view.findViewById<ImageButton>(R.id.logout).setOnClickListener {

            mAuth.signOut()
            Toast.makeText(requireContext(), "Logged Out", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), Activity2::class.java)
            startActivity(intent)
            activity?.finish()
        }

        //var uid = mAuth.currentUser?.uid.toString()
        //val firebaseRef = FirebaseDatabase.getInstance().getReference("users").child(uid.toString())
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Get the username
                    val username = dataSnapshot.child("name").value.toString()
                    val city = dataSnapshot.child("city").value.toString()
                    view.findViewById<TextView>(R.id.name).text = username
                    view.findViewById<TextView>(R.id.location).text = city

                    val image = dataSnapshot.child("image").value.toString()
                    val imageView = view.findViewById<CircleImageView>(R.id.profileImage)
                    if(image.isNotEmpty() && image != "null") {
                        Glide.with(this@Profile_fragment).load(image).into(imageView)
                    }
                    else
                    {
                        imageView.setImageResource(R.drawable.profile_modified)
                    }

                    val coverImage = dataSnapshot.child("coverImage").value.toString()
                    val coverImageView = view.findViewById<ImageView>(R.id.editProfile)
                    if(coverImage.isNotEmpty() && coverImage != "null") {
                        Glide.with(this@Profile_fragment).load(coverImage).into(coverImageView)
                    }
                    else
                    {
                        coverImageView.setImageResource(R.drawable.editprofile)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        val selectImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if(uri != null) {
                val storageRef = FirebaseStorage.getInstance().reference.child("images/${mAuth.currentUser?.uid.toString()}")
                storageRef.putFile(uri).addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener {
                        val url = it.toString()
                        val firebaseRef = FirebaseDatabase.getInstance().getReference("users").child(uid.toString())
                        firebaseRef.child("image").setValue(url).addOnSuccessListener {

                        // Load the image into the ImageView
                        val imageView = view.findViewById<CircleImageView>(R.id.profileImage)
                        Glide.with(this).load(url).into(imageView)
                        }
                    }
                }.addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failed to upload image: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        val selectCoverLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if(uri != null) {
                val storageRef = FirebaseStorage.getInstance().reference.child("coverImages/${mAuth.currentUser?.uid.toString()}")
                storageRef.putFile(uri).addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener {
                        val url = it.toString()
                        val firebaseRef = FirebaseDatabase.getInstance().getReference("users").child(uid.toString())
                        firebaseRef.child("coverImage").setValue(url).addOnSuccessListener {

                            // Load the image into the ImageView
                            val imageView = view.findViewById<ImageView>(R.id.editProfile)
                            Glide.with(this).load(url).into(imageView)
                        }
                    }
                }.addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failed to upload image: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }

        }

        view.findViewById<Button>(R.id.iconButton).setOnClickListener {

            selectImageLauncher.launch("image/*")
            /*val intent = Intent(requireContext(), Activity12::class.java)
            startActivity(intent)*/

            //selectAndUploadImage(view, "images", R.drawable.profile_modified)
        }

        view.findViewById<Button>(R.id.iconButton2).setOnClickListener {
            selectCoverLauncher.launch("image/*")
            //selectAndUploadImage(view, "coverImages", R.drawable.editprofile)
        }

        val mentors = ArrayList<Mentor>()
        val recyclerView = view.findViewById<RecyclerView>(R.id.favoriteMentorsRecyclerView)

        val favoritesRef = FirebaseDatabase.getInstance().getReference("favorites").child(uid.toString())
        val mentorsRef = FirebaseDatabase.getInstance().getReference("Mentors")

        favoritesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val favoriteMentorUids = dataSnapshot.children.map { it.key ?: "" }
                    mentorsRef.get().addOnSuccessListener { mentorSnapshot ->
                        val allMentors = mentorSnapshot.children.mapNotNull { Pair(it.key, it.getValue(Mentor::class.java)) }
                        val favoriteMentors = allMentors.filter { favoriteMentorUids.contains(it.first) }.mapNotNull { it.second }
                        // Now, favoriteMentors contains the mentors whose UIDs are in the favorites.
                        // You can update your RecyclerView adapter with this list.
                        (recyclerView.adapter as MentorAdapter).updateMentors(favoriteMentors)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(null, "Failed to read value.", Toast.LENGTH_SHORT).show()
            }
        })
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = MentorAdapter(mentors)

        //review recycler view
        val reviews = ArrayList<Review>()
        val review_recyclerView = view.findViewById<RecyclerView>(R.id.myReviewsRecyclerView)

        val reviewsRef = FirebaseDatabase.getInstance().getReference("reviews")
        reviewsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val reviews = mutableListOf<Review>()
                    // Loop through each child (each child is a review keyed by the user's UID)
                    for (uidSnapshot in dataSnapshot.children) {
                        // get the Review object from the UID snapshot
                        val review = uidSnapshot.getValue(Review::class.java)
                        if (review != null) {
                            reviews.add(review)
                        }
                    }
                    review_recyclerView.adapter = ReviewAdapter(reviews)
                    review_recyclerView.adapter?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle possible errors
                Log.e("Firebase", "Error fetching data", databaseError.toException())
            }
        })

        review_recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        review_recyclerView.adapter = ReviewAdapter(reviews)
    }

}