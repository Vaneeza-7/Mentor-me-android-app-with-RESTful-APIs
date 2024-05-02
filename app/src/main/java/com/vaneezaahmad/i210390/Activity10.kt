package com.vaneezaahmad.i210390

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.jaredrummler.materialspinner.MaterialSpinner

class Activity10 : AppCompatActivity() {
    var ddp: String = "";
    var vid: String = "";
    var imageUri :Uri? = null;
    var videoUri:Uri? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_10)

        val name = findViewById<EditText>(R.id.name)
        val email = findViewById<EditText>(R.id.email)
        val role = findViewById<EditText>(R.id.role)
        val desc = findViewById<EditText>(R.id.description)
        val price = findViewById<EditText>(R.id.price)
        val pass = findViewById<EditText>(R.id.password)
        val upload = findViewById<TextView>(R.id.upload)
        val camera = findViewById<ImageView>(R.id.camera)
        val video = findViewById<ImageView>(R.id.mentor)
        val videoView = findViewById<VideoView>(R.id.videoView)


        val back = findViewById<ImageView>(R.id.arrow)
        back.setOnClickListener {
            /* startActivity(
                Intent(this, Activity7::class.java)
            );*/
            finish();
        }

        val login = findViewById<TextView>(R.id.login)
        login.setOnClickListener {
            startActivity(
                Intent(this, MentorLogin::class.java)
            );
        }

        val spinner = findViewById<View>(R.id.status) as MaterialSpinner
        spinner.setItems("Status", "Available", "Busy", "Away", "Offline")
        spinner.setOnItemSelectedListener { view, position, id, item ->
            Snackbar.make(
                view,
                "Selected $item",
                Snackbar.LENGTH_LONG
            ).show()
        }

        val spinner_category = findViewById<View>(R.id.category) as MaterialSpinner
        spinner_category.setItems(
            "Education",
            "Entrepreneurship",
            "Personal Growth",
            "Career Development",
            "Technology",
            "Health",
            "Music",
            "Sports",
            "Cooking",
            "Fashion",
            "Other"
        )
        spinner_category.setOnItemSelectedListener { view, position, id, item ->
            Snackbar.make(
                view,
                "Selected $item",
                Snackbar.LENGTH_LONG
            ).show()
        }

        /*nextvid.setOnClickListener {
            startActivity(
                Intent(this, Activity13::class.java)
            );
        }*/

        val pickImage =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                    imageUri = result.data?.data
                    camera.setImageURI(imageUri)
                    /*val storage = FirebaseStorage.getInstance()
                    val storageRef = storage.reference.child("MentorDpImages/${mAuth.currentUser?.uid.toString()}")
                    storageRef.putFile(imageUri!!)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT)
                                .show()
                            storageRef.downloadUrl.addOnSuccessListener {
                                ddp = it.toString()
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT)
                                .show()
                        }*/
                }

            }

        val pickVideo =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                    videoUri = result.data?.data
                    videoView.setVideoURI(videoUri)
                    videoView.setOnPreparedListener {
                        video.visibility = View.GONE
                        videoView.start()
                    }
                    /* val storage = FirebaseStorage.getInstance()
                     val storageRef =
                         storage.reference.child("MentorVideo/${mAuth.currentUser?.uid.toString()}")
                     storageRef.putFile(videoUri!!)
                         .addOnSuccessListener {
                             Toast.makeText(this, "Video uploaded successfully", Toast.LENGTH_SHORT)
                                 .show()
                             storageRef.downloadUrl.addOnSuccessListener {
                                 ddp = it.toString()
                             }
                         }
                         .addOnFailureListener {
                             Toast.makeText(this, "Failed to upload video", Toast.LENGTH_SHORT)
                                 .show()
                         }*/
                }
            }


        camera.setOnClickListener {
            pickImage.launch(
                Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
            )

        }

        video.setOnClickListener {
            pickVideo.launch(
                Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                )
            )
        }

        upload.setOnClickListener {
            val nameStr = name.text.toString()
            val emailStr = email.text.toString()
            val passStr = pass.text.toString()
            val roleStr = role.text.toString()
            val descStr = desc.text.toString()
            val priceStr = price.text.toString()
            val statusStr = spinner.text.toString()
            val categoryStr = spinner_category.text.toString()

            if (nameStr.isNotEmpty() && emailStr.isNotEmpty() && passStr.isNotEmpty() && roleStr.isNotEmpty() && descStr.isNotEmpty() && priceStr.isNotEmpty() && statusStr.isNotEmpty() && categoryStr.isNotEmpty()) {
            }
        }

    }
}