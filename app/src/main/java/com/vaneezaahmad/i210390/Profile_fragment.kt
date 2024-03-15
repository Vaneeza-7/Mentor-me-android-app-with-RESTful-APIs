package com.vaneezaahmad.i210390

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
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



        view.findViewById<CardView>(R.id.card).setOnClickListener {
            val intent = Intent(requireContext(), Activity8::class.java)
            startActivity(intent)
        }


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

        var uid = mAuth.currentUser?.uid.toString()
        val firebaseRef = FirebaseDatabase.getInstance().getReference("users").child(uid.toString())
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
                        firebaseRef.child("image").setValue(url)

                        // Load the image into the ImageView
                        val imageView = view.findViewById<CircleImageView>(R.id.profileImage)
                        Glide.with(this).load(url).into(imageView)
                    }
                }
            }
        }

        view.findViewById<Button>(R.id.iconButton).setOnClickListener {

            selectImageLauncher.launch("image/*")
            /*val intent = Intent(requireContext(), Activity12::class.java)
            startActivity(intent)*/
        }

        view.findViewById<Button>(R.id.iconButton2).setOnClickListener {
            //selectImageLauncher.launch("image/*")
        }

    }
}