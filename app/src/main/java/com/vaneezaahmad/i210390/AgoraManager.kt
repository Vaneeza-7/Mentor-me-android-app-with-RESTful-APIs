package com.vaneezaahmad.i210390

import io.agora.rtc2.*
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.view.SurfaceView
import android.view.View.GONE
import android.widget.Toast
import androidx.core.content.ContextCompat
import io.agora.rtc2.video.VideoCanvas

class AgoraManager   {

    private var rtcEngine: RtcEngine? = null
    private val PERMISSION_id = 22
    private val REQUESTED_PERMISSIONS = arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)
    val appId = "d0e718318bd9428fba16e31d2eb96b21"
    private val channelName = "vaneezaMentorMe"
    private val token = "007eJxTYPBYJnCL7/GanG6FGOvzJ33PaGp8mmXIOyGq28d3bw7P1O8KDCkGqeaGFsaGFkkpliZGFmlJiYZmqcaGKUapSZZmSUaGogf/pzYEMjIIXg1gYIRCEJ+foSwxLzW1KtE3Na8kv8g3lYEBAHWNIvM="
    private val uid = 0
    private var isJoined = false
    private var isMuted = false
    private var isVideoMuted = false
    private var localSurfaceView : SurfaceView? = null
    private var remoteSurfaceView : SurfaceView? = null

    fun initAgoraEngine(context: Context, appId: String) {
        try {
            rtcEngine = RtcEngine.create(context, appId, object : IRtcEngineEventHandler() {
                override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
                    super.onJoinChannelSuccess(channel, uid, elapsed)
                    isJoined = true
                    showMessage(context, "You have joined the channel")
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

    fun joinChannel(channelName: String, uid: Int) {
        val channelOption = ChannelMediaOptions()
        channelOption.channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION
        channelOption.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER
        //enableLocalVideo()


        rtcEngine?.joinChannel(token, channelName, null, uid)
    }

    fun leaveChannel() {
        rtcEngine?.leaveChannel()
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
        RtcEngine.destroy()
        rtcEngine = null
    }

    fun checkPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    fun showMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun setupRemoteVideo(context: Context, uid: Int, remoteSurfaceView: SurfaceView) {
        rtcEngine?.setupRemoteVideo(VideoCanvas(remoteSurfaceView, Constants.RENDER_MODE_FIT, uid))
    }

    fun setupLocalVideo(context: Context, localSurfaceView: SurfaceView) {
        rtcEngine?.setupLocalVideo(VideoCanvas(localSurfaceView, Constants.RENDER_MODE_FIT, uid))
    }

    fun runOnUiThread(runnable: Runnable) {
        Thread(runnable).start()
    }
}