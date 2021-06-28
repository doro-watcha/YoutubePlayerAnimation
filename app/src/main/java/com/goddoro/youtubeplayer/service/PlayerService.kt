package com.goddoro.youtubeplayer.service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Binder
import android.os.IBinder
import com.goddoro.youtubeplayer.CommonConst.ACTION_PLAYER_PAUSE
import com.goddoro.youtubeplayer.CommonConst.ACTION_PLAYER_PLAY
import com.goddoro.youtubeplayer.utils.NotificationUtil
import com.goddoro.youtubeplayer.utils.debugE
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer

class PlayerService : Service() {


    private val TAG = PlayerService::class.java.simpleName


    private val binder = PlayerBinder()

    lateinit var player : SimpleExoPlayer

    lateinit var notificationUtil : NotificationUtil

    override fun onBind(p0: Intent?): IBinder {

        debugE(TAG, "onBind")

        player = SimpleExoPlayer.Builder(this).build()

        notificationUtil = NotificationUtil(this)

        val mediaItem =
            MediaItem.fromUri("https://cdn.onesongtwoshows.com/video/okt7ne01ywn_1602269794509.mp4")
        player.setMediaItem(mediaItem)
        player.prepare()

        return binder
    }


    fun play() {
        player.play()
        notificationUtil.updateNotification(notificationUtil.getNotificationBuilder(true))
    }

    fun pause() {
        player.pause()
        notificationUtil.updateNotification(notificationUtil.getNotificationBuilder(false))
    }



    inner class PlayerBinder : Binder() {

        fun getService(): PlayerService = this@PlayerService

    }

    val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            when (intent.action) {
                ACTION_PLAYER_PLAY -> {
                    play()
                }
                ACTION_PLAYER_PAUSE -> {
                    pause()
                }

            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()


        player.release()
    }
}