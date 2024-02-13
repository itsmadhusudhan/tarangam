package com.shreekaram.tarangam.modules.songs

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.shreekaram.tarangam.MainActivity
import com.shreekaram.tarangam.R
import com.shreekaram.tarangam.modules.songs.domain.Song

private const val ACTION_PAUSE = "com.shreekaram.tarangam.action.pause"

class MusicService : Service() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("com.shreekaram.tarangam.modules.songs.MusicService started")

        val action: String? = intent?.action

        if (action != null) {
            when (action) {
                ACTION_PAUSE -> {
                    mediaPlayer?.pause()
                }
            }
            return START_NOT_STICKY
        }

        val song: Song = intent?.getParcelableExtra("song") ?: return START_NOT_STICKY

        mediaPlayer?.release()

        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA).build()
            )
            setDataSource(song.path)
            prepare()
            start()
        }


        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )


        val pauseIntent = Intent(this, MusicService::class.java).apply {
            this.action = ACTION_PAUSE
        }

        val pausePendingIntent = PendingIntent.getService(
            this, 0, pauseIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setChannelId("music")
            .setContentTitle(song.title)
            .setContentText(song.artist)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .setSilent(true)
            .addAction(
                R.drawable.ic_launcher_foreground, "Pause",
                pausePendingIntent
            )
            .build()

        startForeground(1, notification)

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        const val CHANNEL_ID = "MusicServiceChannel"
    }
}