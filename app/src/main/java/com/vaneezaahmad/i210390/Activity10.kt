package com.vaneezaahmad.i210390

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.result.contract.ActivityResultContracts
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.jaredrummler.materialspinner.MaterialSpinner
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class Activity10 : AppCompatActivity() {
    var ddp: String = "";
    var vid: String = "";
    var imageUri :Uri? = null;
    var videoUri:Uri? = null;
    private var imageString: String? = null
    private var videoString: String? = null
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
                        val bitmap = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                            MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri!!)
                        } else {
                            val source = ImageDecoder.createSource(this.contentResolver, imageUri!!)
                            ImageDecoder.decodeBitmap(source)
                        }
                        // Convert the bitmap to Base64 string
                        imageString = bitmapToBase64(bitmap)
                        //uploadImage(imageString!!, email!!, "DP")
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
                    val videoBytes = contentResolver.openInputStream(videoUri!!)?.readBytes()
                    videoString = Base64.encodeToString(videoBytes, Base64.DEFAULT)
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
        ///////////////add image and video to database/////////////////////
        ////also ake php files for this////

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
                val url = getString(R.string.IP) + "mentorme/addMentor.php"
                val request = object : StringRequest(
                    Method.POST, url,
                    { response ->
                        val JsonResponse = JSONObject(response)
                        val status = JsonResponse.getInt("status")
                        if (status == 1) {

                            Toast.makeText(this, "Mentor registered successfully", Toast.LENGTH_SHORT).show()
                            if (imageString != null) {
                                uploadImage(imageString!!, emailStr)
                               // Toast.makeText(this, "Image uploaded", Toast.LENGTH_SHORT).show()
                            }
                            if (videoString != null) {
                                uploadVideo(videoString!!, emailStr)
                                val intent = Intent(this, ActivityMentor::class.java)
                                intent.putExtra("email", emailStr)
                                startActivity(intent)
                            }
                            else
                            {
                                Toast.makeText(this, "Couldn't upload video", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, ActivityMentor::class.java)
                                intent.putExtra("email", emailStr)
                                startActivity(intent)
                            }

                        } else {
                            Toast.makeText(this, "Couldn't register mentor", Toast.LENGTH_SHORT).show()
                        }
                    },
                    { error ->
                        Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                    }
                ) {
                    override fun getParams(): MutableMap<String, String> {
                        val map = HashMap<String, String>()
                        map["email"] = emailStr
                        map["name"] = nameStr
                        map["password"] = passStr
                        map["role"] = roleStr
                        map["description"] = descStr
                        map["price"] = priceStr
                        map["status"] = statusStr
                        map["category"] = categoryStr
                        return map
                    }
                }
                Volley.newRequestQueue(this).add(request)
            }
        }

    }

    fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun uploadImage(image: String, email: String) {

        val url = getString(R.string.IP) + "mentorme/uploaddpMentor.php"
        val requestQueue = Volley.newRequestQueue(this)

        val imageRequest = object : StringRequest(Method.POST, url,
            { response ->
                val jsonResponse = JSONObject(response)
                val status = jsonResponse.getInt("status")
                val message = jsonResponse.getString("message")
                if(status == 1) {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Log.d("ImageError", error.toString())
                Toast.makeText(this, "An error occured", Toast.LENGTH_SHORT).show()
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

    private fun uploadVideo(video: String, email: String) {
        val url = getString(R.string.IP) + "mentorme/uploadVideoMentor.php"
        val requestQueue = Volley.newRequestQueue(this)

        val videoRequest = object : StringRequest(Method.POST, url,
            { response ->
                val jsonResponse = JSONObject(response)
                val status = jsonResponse.getInt("status")
                val message = jsonResponse.getString("message")
                if(status == 1) {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Log.d("VideoError", error.toString())
                Toast.makeText(this, "An error occured", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["video"] = video
                params["email"] = email
                params["localhost"] = getString(R.string.IP)
                return params
            }
        }
        requestQueue.add(videoRequest)


    }


}