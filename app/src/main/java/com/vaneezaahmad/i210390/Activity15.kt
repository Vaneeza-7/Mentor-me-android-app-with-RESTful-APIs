package com.vaneezaahmad.i210390

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class Activity15 : AppCompatActivity() {
    private lateinit var timer: CountDownTimer
    val PERMISSION_ID = 22
    val REQUESTED_PERMISSIONS = arrayOf(Manifest.permission.RECORD_AUDIO)
    private lateinit var agoraManager:AgoraManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_15)

        var time = findViewById<TextView>(R.id.time)
        timer = object : CountDownTimer(300000, 1000) {
            var second = 0
            var minute = 0
            var hour = 0
            override fun onTick(millisUntilFinished: Long) {
                second++;
                if (second == 60) {
                    second = 0
                    minute++
                }
                if (minute == 60) {
                    minute = 0
                    hour++
                }
                time.text = String.format("%02d:%02d:%02d", hour, minute, second)
            }

            override fun onFinish() {
                time.text = "00:00:00"
            }
        }
        timer.start()

        var name = findViewById<TextView>(R.id.name)
        var profileImage = findViewById<CircleImageView>(R.id.profileImage)
        val mentorName = intent.getStringExtra("mentorName")
        val mentorImage = intent.getStringExtra("mentorImage")
        val userName = intent.getStringExtra("userName")
        val userImage = intent.getStringExtra("userImage")

        if(mentorName != null) {
            name.text = userName
            Glide.with(this).load(userImage).into(profileImage)
        }
        else {
            name.text = mentorName
            Glide.with(this).load(mentorImage).into(profileImage)
        }

        agoraManager = AgoraManager()
        var muteButton = findViewById<ImageButton>(R.id.mutebutton)
        var leaveButton = findViewById<ImageButton>(R.id.LeaveCall)
        var speaker = findViewById<ImageButton>(R.id.speaker)
        var pauseButton = findViewById<ImageButton>(R.id.pauseButton)
        val appId = "d0e718318bd9428fba16e31d2eb96b21"
        val channelName = "vaneezaMentorMe"
        val token = "007eJxTYDDoOmpnYPvnQdJNG7VPbQlOR0wFhf+ZvD927/21q34fN6opMKQYpJobWhgbWiSlWJoYWaQlJRqapRobphilJlmaJRkZSrkypTUEMjLEy+9kYIRCEJ+foSwxLzW1KtE3Na8kv8g3lYEBABbdJBI="
        val uid = 0
        var isMuted = false

        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // Permissions are not granted, request them.
            ActivityCompat.requestPermissions(this,REQUESTED_PERMISSIONS, PERMISSION_ID)
        }
        else {
            agoraManager.initAgoraEngineForAudioCall(this, appId)
            agoraManager.joinChannelForAudioCall(channelName, uid, this)
        }

        muteButton.setOnClickListener {
            if (isMuted) {
                agoraManager.muteLocalAudioStream(isMuted)
                isMuted = false
            } else {
                agoraManager.muteLocalAudioStream(isMuted)
                isMuted = true
            }
        }

        /*speaker.setOnClickListener {
            if (isMuted) {
                agoraManager.muteAllRemoteAudioStreams(isMuted)
                isMuted = false
            } else {
                agoraManager.muteAllRemoteAudioStreams(isMuted)
                isMuted = true
            }
        }*/

        leaveButton.setOnClickListener {
            agoraManager.leaveChannelForAudioCall(this)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        agoraManager.destroy()
    }
}