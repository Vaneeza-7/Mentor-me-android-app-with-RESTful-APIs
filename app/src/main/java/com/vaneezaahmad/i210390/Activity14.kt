package com.vaneezaahmad.i210390

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceView
import android.widget.FrameLayout
import android.widget.ImageButton
import com.google.firebase.auth.FirebaseAuth
import com.vaneezaahmad.i210390.AgoraManager
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class Activity14 : AppCompatActivity() {
    val mAuth = FirebaseAuth.getInstance()
    val uid = mAuth.currentUser?.uid
    private lateinit var agoraManager:AgoraManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_14)
        agoraManager = AgoraManager()
        var localSurfaceView = SurfaceView(this)
        findViewById<FrameLayout>(R.id.local_user).addView(localSurfaceView)

        var remoteSurfaceView = SurfaceView(this)
        findViewById<FrameLayout>(R.id.remote_user).addView(remoteSurfaceView)

        var muteButton = findViewById<ImageButton>(R.id.mutebutton)
        var videoButton = findViewById<ImageButton>(R.id.videoButton)
        var switchButton = findViewById<ImageButton>(R.id.switchButton)
        var leaveButton = findViewById<ImageButton>(R.id.LeaveCall)

        val appId = "d0e718318bd9428fba16e31d2eb96b21"
        val channelName = "vaneezaMentorMe"
        val token = "007eJxTYDDoOmpnYPvnQdJNG7VPbQlOR0wFhf+ZvD927/21q34fN6opMKQYpJobWhgbWiSlWJoYWaQlJRqapRobphilJlmaJRkZSrkypTUEMjLEy+9kYIRCEJ+foSwxLzW1KtE3Na8kv8g3lYEBABbdJBI="
        val uid = 0
        var isMuted = false
        var isVideoMuted = false
        val PERMISSION_ID = 22
        val REQUESTED_PERMISSIONS = arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)


        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
            || checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Permissions are not granted, request them.
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, PERMISSION_ID)
        }
        else {
            agoraManager.initAgoraEngine(this, appId, localSurfaceView, remoteSurfaceView)
            agoraManager.joinChannel(channelName, uid, this, localSurfaceView)
        }
        muteButton.setOnClickListener {
            agoraManager.muteLocalAudioStream(isMuted)
        }

        videoButton.setOnClickListener {
            agoraManager.muteLocalVideoStream(isVideoMuted)
        }

        switchButton.setOnClickListener {
            agoraManager.switchCamera()
        }

        leaveButton.setOnClickListener {
            agoraManager.leaveChannel(this, localSurfaceView, remoteSurfaceView)
            finish()
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        agoraManager.destroy()
    }
}