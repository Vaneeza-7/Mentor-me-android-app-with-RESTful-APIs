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
import android.os.CountDownTimer
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat

class Activity14 : AppCompatActivity() {
    val mAuth = FirebaseAuth.getInstance()
    val uid = mAuth.currentUser?.uid
    private lateinit var agoraManager:AgoraManager
    private lateinit var timer: CountDownTimer
    val PERMISSION_ID = 22
    val REQUESTED_PERMISSIONS = arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_14)

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
            isMuted = !isMuted
            agoraManager.muteLocalAudioStream(isMuted)
            //muteButton.setImageResource(if (isMuted) R.drawable.ic_baseline_mic_off_24 else R.drawable.ic_baseline_mic_24)
        }

        videoButton.setOnClickListener {
            isVideoMuted = !isVideoMuted
            agoraManager.muteLocalVideoStream(isVideoMuted)
            //videoButton.setImageResource(if (isVideoMuted) R.drawable.ic_baseline_videocam_off_24 else R.drawable.ic_baseline_videocam_24)
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permissions have been granted, initialize Agora.
                agoraManager.initAgoraEngine(this, "d0e718318bd9428fba16e31d2eb96b21", SurfaceView(this), SurfaceView(this))
                agoraManager.joinChannel("vaneezaMentorMe", 0, this, SurfaceView(this))
            } else {
                // Permissions were denied. Handle the error case.
                Toast.makeText(this, "Permissions Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}