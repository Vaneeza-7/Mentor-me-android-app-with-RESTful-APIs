package com.vaneezaahmad.i210390

import io.agora.rtc2.*
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.view.SurfaceView
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.core.content.ContextCompat
import io.agora.rtc2.video.VideoCanvas

class AgoraManager   {

    private var rtcEngine: RtcEngine? = null
    //private val PERMISSION_id = 22
    //private val REQUESTED_PERMISSIONS = arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)
    private val appId = ""
    private val channelName = "vaneezaMentorMe"
    private val token = "007eJxTYDDoOmpnYPvnQdJNG7VPbQlOR0wFhf+ZvD927/21q34fN6opMKQYpJobWhgbWiSlWJoYWaQlJRqapRobphilJlmaJRkZSrkypTUEMjLEy+9kYIRCEJ+foSwxLzW1KtE3Na8kv8g3lYEBABbdJBI="
    private val uid = 0
    private var isJoined = false
    private var isMuted = false
    private var isVideoMuted = false

    fun initAgoraEngine(context: Context, appId: String, localSurfaceView: SurfaceView, remoteSurfaceView: SurfaceView) {
        try {
            rtcEngine = RtcEngine.create(context, appId, object : IRtcEngineEventHandler() {
                override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
                    super.onJoinChannelSuccess(channel, uid, elapsed)
                    isJoined = true
                    showMessage(context, "You have joined the channel")
                    Log.d("Agora", "Joined channel successfully. Channel: $channel, UID: $uid")
                }

                override fun onUserJoined(uid: Int, elapsed: Int) {
                    super.onUserJoined(uid, elapsed)
                    showMessage(context, "Remote User $uid joined")
                    runOnUiThread {
                        setupRemoteVideo(context, uid, remoteSurfaceView!!)
                    }
                }

                override fun onUserOffline(uid: Int, reason: Int) {
                    super.onUserOffline(uid, reason)
                    showMessage(context, "Remote User $uid Offline")
                    runOnUiThread{
                        remoteSurfaceView!!.visibility = GONE
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }

        rtcEngine?.enableVideo()
    }

    fun joinChannel(channelName: String, uid: Int, context: Context, localSurfaceView: SurfaceView) {

               val channelOption = ChannelMediaOptions()
               channelOption.channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION
               channelOption.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER
               setupLocalVideo(context, localSurfaceView!!)
               rtcEngine?.startPreview()
               rtcEngine?.joinChannel(token, channelName, null, uid)
    }

    fun leaveChannel(context: Context, localSurfaceView: SurfaceView, remoteSurfaceView: SurfaceView) {
        if(!isJoined){
            showMessage(context, "You are not in a channel")
        }
        else{
            rtcEngine?.leaveChannel()
            showMessage(context, "You have left the channel")
            if(remoteSurfaceView!=null)
                remoteSurfaceView!!.visibility = GONE
            if(localSurfaceView!=null)
                localSurfaceView!!.visibility = GONE
            isJoined=false
        }


    }

    fun initAgoraEngineForAudioCall(context: Context, appId: String) {
        try {
            rtcEngine = RtcEngine.create(context, appId, object : IRtcEngineEventHandler() {
                override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
                    super.onJoinChannelSuccess(channel, uid, elapsed)
                    isJoined = true
                    showMessage(context, "Audio call joined successfully")
                }

                override fun onUserOffline(uid: Int, reason: Int) {
                    super.onUserOffline(uid, reason)
                    showMessage(context, "User $uid offline")
                }

                // Add more handlers if needed
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun joinChannelForAudioCall(channelName: String, uid: Int, context: Context) {
        // Check for RECORD_AUDIO permission
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            val channelOption = ChannelMediaOptions()
            channelOption.channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION
            channelOption.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER
            rtcEngine?.joinChannel(token, channelName, null, uid)
        } else {
            // Request for RECORD_AUDIO permission or inform the user they need to grant it
            showMessage(context, "Audio permission is required for audio calls.")
        }
    }

    fun leaveChannelForAudioCall(context: Context) {
        if (!isJoined) {
            showMessage(context, "You are not in a channel")
        } else {
            rtcEngine?.leaveChannel()
            showMessage(context, "You have left the channel")
            isJoined = false
        }
    }

    fun muteLocalAudioStream(isMute: Boolean) {
        rtcEngine?.muteLocalAudioStream(isMute)

    }

    fun muteLocalVideoStream(isMute: Boolean) {
        rtcEngine?.muteLocalVideoStream(isMute)
    }

    fun enableLocalVideo(isEnable: Boolean) {
        rtcEngine?.enableLocalVideo(isEnable)
    }

    fun switchCamera() {
        rtcEngine?.switchCamera()
    }

    fun destroy() {
        rtcEngine!!.stopPreview()
        rtcEngine!!.leaveChannel()
        Thread {
            RtcEngine.destroy()
            rtcEngine = null
        }.start()
    }

    /*fun checkPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }*/

    fun showMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun setupRemoteVideo(context: Context, uid: Int, remoteSurfaceView: SurfaceView) {
        rtcEngine?.setupRemoteVideo(VideoCanvas(remoteSurfaceView, Constants.RENDER_MODE_FIT, uid))
    }

    fun setupLocalVideo(context: Context, localSurfaceView: SurfaceView) {
        rtcEngine?.setupLocalVideo(VideoCanvas(localSurfaceView, Constants.RENDER_MODE_FIT, uid))
        localSurfaceView.visibility = VISIBLE
    }

    fun runOnUiThread(runnable: Runnable) {
        Thread(runnable).start()
    }
}