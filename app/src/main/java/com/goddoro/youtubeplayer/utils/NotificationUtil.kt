package com.goddoro.youtubeplayer.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.goddoro.youtubeplayer.CommonConst.ACTION_PLAYER_CLOSE
import com.goddoro.youtubeplayer.CommonConst.ACTION_PLAYER_NEXT
import com.goddoro.youtubeplayer.CommonConst.ACTION_PLAYER_PAUSE
import com.goddoro.youtubeplayer.CommonConst.ACTION_PLAYER_PLAY
import com.goddoro.youtubeplayer.CommonConst.ACTION_PLAYER_PREV
import com.goddoro.youtubeplayer.CommonConst.CLOSE_INTENT_REQUEST
import com.goddoro.youtubeplayer.CommonConst.CONTENT_INTENT_REQUEST
import com.goddoro.youtubeplayer.CommonConst.NEXT_INTENT_REQUEST
import com.goddoro.youtubeplayer.CommonConst.PAUSE_INTENT_REQUEST
import com.goddoro.youtubeplayer.CommonConst.PLAY_INTENT_REQUEST
import com.goddoro.youtubeplayer.CommonConst.PREV_INTENT_REQUEST
import com.goddoro.youtubeplayer.MainActivity
import com.goddoro.youtubeplayer.R

class NotificationUtil (
    private val context: Context
) {

    private val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            var channel: NotificationChannel?=
                notificationManager.getNotificationChannel(context.resources.getString(R.string.channel_id))

            if ( channel == null) {
                channel = NotificationChannel(context.resources.getString(R.string.channel_id),context.resources.getString(R.string.channel_name),
                    NotificationManager.IMPORTANCE_LOW)
                channel.description = context.resources.getString(R.string.channel_description)
                channel.setShowBadge(false)
                notificationManager.createNotificationChannel(channel)
            }

        }
    }

    fun getNotificationBuilder(isPlaying : Boolean? = true ): Notification {

        createNotificationChannel()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val builder = NotificationCompat.Builder(context, context.resources.getString(R.string.channel_id))

            val contentIntent = Intent(context, MainActivity::class.java)
            val contentPendingIntent =
                PendingIntent.getActivity(context, CONTENT_INTENT_REQUEST, contentIntent, 0)

            val pausePendingIntent = PendingIntent.getBroadcast(context, PAUSE_INTENT_REQUEST, Intent(ACTION_PLAYER_PAUSE), 0)
            val playPendingIntent = PendingIntent.getBroadcast(context, PLAY_INTENT_REQUEST, Intent(ACTION_PLAYER_PLAY), 0)
            val closePendingIntent = PendingIntent.getBroadcast(context, CLOSE_INTENT_REQUEST, Intent(ACTION_PLAYER_CLOSE), 0)
            val prevPendingIntent = PendingIntent.getBroadcast(context, PREV_INTENT_REQUEST, Intent(ACTION_PLAYER_PREV), 0)
            val nextPendingIntent = PendingIntent.getBroadcast(context, NEXT_INTENT_REQUEST, Intent(ACTION_PLAYER_NEXT), 0)

            builder
                .setSmallIcon(R.drawable.ic_ondemand)
                .setLargeIcon(ResourcesCompat.getDrawable(context.resources, R.drawable.ic_ondemand,null)?.toBitmap())
                .setContentTitle(context.resources.getString(R.string.notification_content_title))
                .setContentText(context.resources.getString(R.string.notification_content_text))

                .setContentIntent(contentPendingIntent)
                .addAction(createAction(R.drawable.btn_prev,"prev",prevPendingIntent))
                .addAction(if ( isPlaying == false ) createAction(R.drawable.btn_play,"play",playPendingIntent) else
                    createAction(R.drawable.btn_pause,"pause",pausePendingIntent))
                .addAction(createAction(R.drawable.btn_next,"next",nextPendingIntent))
                .addAction(createAction(R.drawable.btn_close,"close",closePendingIntent))

            if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.setStyle(
                    androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(1,2,3 /* #1: pause button \*/)
                    .setMediaSession(MediaSessionCompat(context,"mediasession").sessionToken))
            }


            return builder.build()

        } else {
            throw Error()
        }
    }

    fun updateNotification ( notification : Notification) {
        notificationManager.notify(1,notification)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun createAction (@DrawableRes resource : Int, title : String, intent : PendingIntent) : NotificationCompat.Action {
        return NotificationCompat.Action(resource, title, intent)

    }

}