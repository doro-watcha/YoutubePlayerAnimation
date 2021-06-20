package com.goddoro.youtubeplayer.service

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Binder
import android.os.IBinder
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer

class PlayerService : Service() {


    private val TAG = PlayerService::class.java.simpleName


    private val binder = PlayerBinder()

    lateinit var player : SimpleExoPlayer

    override fun onBind(p0: Intent?): IBinder {

        player = SimpleExoPlayer.Builder(this).build()

        val mediaItem =
            MediaItem.fromUri("https://1tvsd.gscdn.kbs.co.kr/1tv-02/1tv-02_sd.m3u8?Expires=1623804718&Policy=eyJTdGF0ZW1lbnQiOlt7IlJlc291cmNlIjoiaHR0cHM6Ly8xdHZzZC5nc2Nkbi5rYnMuY28ua3IvMXR2LTAyLzF0di0wMl9zZC5tM3U4IiwiQ29uZGl0aW9uIjp7IkRhdGVMZXNzVGhhbiI6eyJBV1M6RXBvY2hUaW1lIjoxNjIzODA0NzE4fX19XX0_&Signature=j3-BFS8wWMgRecFXLsoVIt1lT5RrVjtkhmEGKu0GFo7DdhEC6IZMCPjgp1ci4edtms~ZkU-m1EvnjUdCfVkDQOfCf4kZZcKKojVd5yw2d1vTgwzdNcwultmvwKKsoqEtbSvQotLx8TFXnbF2Wh0lVwdxfh-lGaEJKhYqkhuxiX1Y~XfAZT0WroYnLJY9IOm7WGx~KTSigqNzFm8IOWIJqbpVVO3tmjCR6shSQsPbvU7rfVBYz9PXCJi7cMtTs5zTcrU3IO0PNRZsd~FoTpsHbaNshgbSHit-3zLvgYH1YQTdsxgoC130lwWsbHjheQdli7AUkUcjWO3PW0Lfh8wUXw__&Key-Pair-Id=APKAICDSGT3Y7IXGJ3TA")

        player.setMediaItem(mediaItem)
        player.prepare()

        return binder
    }


    fun getPlayerFromService() = player

    fun play() {
        //notificationUtil.createNotificationChannel()
        player.play()
    }

    fun pause() {

        player.pause()
    }


    inner class PlayerBinder : Binder() {

        fun getService(): PlayerService = this@PlayerService

    }

    override fun onDestroy() {
        super.onDestroy()


        player.release()
    }
}