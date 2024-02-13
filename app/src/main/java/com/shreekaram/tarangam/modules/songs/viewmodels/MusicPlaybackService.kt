package com.shreekaram.tarangam.modules.songs.viewmodels

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.OptIn
import androidx.core.content.ContextCompat
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.ui.PlayerNotificationManager
import androidx.media3.ui.PlayerNotificationManager.NotificationListener
import com.shreekaram.tarangam.modules.songs.domain.Song

private const val ACTION_PAUSE = "com.shreekaram.tarangam.action.pause"

@OptIn(UnstableApi::class)
class UampNotificationManager(
    private val context: Context,
    notificationId: Int,
    notificationChannelId: String,
    notificationListener: NotificationListener
) {
    private val notificationManager: PlayerNotificationManager = PlayerNotificationManager.Builder(
        context,
        notificationId, notificationChannelId
    )
        .setMediaDescriptionAdapter(object : PlayerNotificationManager.MediaDescriptionAdapter {
            override fun getCurrentContentTitle(player: Player): CharSequence {
                return player.mediaMetadata.albumTitle ?: ""
            }

            override fun createCurrentContentIntent(player: Player): PendingIntent? {
                // return an intent to open the UI when the notification is tapped
                return PendingIntent.getActivity(
                    context,
                    0,
                    Intent(context, MusicPlaybackService::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )
            }

            override fun getCurrentContentText(player: Player): CharSequence? {
                // return the text of the current media item
                return player.mediaMetadata.displayTitle
            }

            override fun getCurrentLargeIcon(
                player: Player,
                callback: PlayerNotificationManager.BitmapCallback
            ): Bitmap? {
                // return the large icon for the current media item
                return null
            }
        })
        .setNotificationListener(notificationListener)
        .build()

    fun hideNotification() {
        notificationManager.setPlayer(null)
    }

    fun showNotificationForPlayer(player: Player) {
        notificationManager.setPlayer(player)
    }

}

class MusicPlaybackService : MediaSessionService() {
    private var mediaSession: MediaSession? = null
    private lateinit var notificationManager: UampNotificationManager
    private var isForegroundService = false

    private val uAmpAudioAttributes = AudioAttributes.Builder()
        .setContentType(
            C.AUDIO_CONTENT_TYPE_MUSIC
        )
        .setUsage(C.USAGE_MEDIA)
        .build()

    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()

        val player = ExoPlayer.Builder(this)
            .build()

//        val sessionActivityPendingIntent =
//            packageManager?.getLaunchIntentForPackage(packageName)?.let { sessionIntent ->
//                PendingIntent.getActivity(
//                    this, 0,
//                    sessionIntent,
//                    PendingIntent.FLAG_UPDATE_CURRENT
//                )
//            }

        mediaSession = MediaSession.Builder(this, player)
//            .setSessionActivity(sessionActivityPendingIntent!!)
            .build()

        notificationManager = UampNotificationManager(
            this,
            1,
            "music",
            PlayerNotificationListener()
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        println("com.shreekaram.tarangam.modules.songs.MusicService started")

        val action: String? = intent?.action

        if (action != null) {
            when (action) {
                ACTION_PAUSE -> {
                    mediaSession?.player?.pause()
                }
            }
            return START_NOT_STICKY
        }

        val song: Song = intent?.getParcelableExtra("song") ?: return START_NOT_STICKY

        println("Playing song: $song")

        val mediaItem = MediaItem.fromUri(Uri.parse(song.path))

        mediaSession?.player?.addMediaItem(mediaItem)

        mediaSession?.player?.prepare()

        val metadata = MediaMetadata.Builder()
            .setAlbumTitle(song.album)
            .setTitle(song.title)
            .build()


        mediaSession?.player?.play()
        println(mediaSession?.player?.playbackState)

        notificationManager.showNotificationForPlayer(mediaSession?.player!!)

        return START_STICKY
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }


    override fun onTaskRemoved(rootIntent: Intent?) {
        val player = mediaSession?.player!!
        if (!player.playWhenReady || player.mediaItemCount == 0) {
            // Stop the service if not playing, continue playing in the background
            // otherwise.
            player.pause()
        }
        stopSelf()
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }


    @UnstableApi
    private inner class PlayerNotificationListener :
        PlayerNotificationManager.NotificationListener {
        override fun onNotificationPosted(
            notificationId: Int,
            notification: Notification,
            ongoing: Boolean
        ) {
            if (ongoing && !isForegroundService) {
                ContextCompat.startForegroundService(
                    applicationContext,
                    Intent(applicationContext, this@MusicPlaybackService.javaClass)
                )

                startForeground(notificationId, notification)
                isForegroundService = true
            }
        }

        override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
            stopForeground(true)
            isForegroundService = false
            stopSelf()
        }
    }

}