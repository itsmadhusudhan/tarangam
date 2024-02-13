package com.shreekaram.tarangam.modules.songs

import android.content.ComponentName
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import com.shreekaram.tarangam.modules.songs.viewmodels.MusicPlaybackService
import com.shreekaram.tarangam.modules.songs.viewmodels.SongsViewModel

@Composable
fun AudioPlayer(viewmodel: SongsViewModel) {
    val context = LocalContext.current
    val playList = viewmodel.audio.collectAsState().value
    val metadata = remember {
        mutableStateOf<MediaMetadata?>(null)
    }
    val mediaController = remember {
        val sessionToken =
            SessionToken(context, ComponentName(context, MusicPlaybackService::class.java))
        val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture.addListener(
            {
                controllerFuture.get().addListener(
                    object : Player.Listener {
                        override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                            super.onMediaMetadataChanged(mediaMetadata)
                            println("MediaMetadataChanged")
                            println(mediaMetadata)

                            metadata.value = mediaMetadata

                        }
                    }
                )
            },
            MoreExecutors.directExecutor()
        )

        controllerFuture
    }

//    val exoPlayer = remember {
//        ExoPlayer.Builder(context).build().apply {
//            this.addListener(
//                object : Player.Listener {
//                    override fun onEvents(player: Player, events: Player.Events) {
//                        super.onEvents(player, events)
//                        if (player.contentPosition >= 200) visible.value = false
//                    }
//
//                    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
//                        super.onMediaItemTransition(mediaItem, reason)
//                        visible.value = true
//                    }
//                },
//            )
//        }
//    }

//    exoPlayer.prepare()
//
//    LaunchedEffect(key1 = playList.count()) {
//        playList.forEach {
////            val path = "android.resource://" + packageName + "/" + it.music
//            println(it.path)
//            val mediaItem = MediaItem.fromUri(Uri.parse(it.path))
//            exoPlayer.addMediaItem(mediaItem)
//        }
//    }
//

    Column {
        Button(onClick = {
            for (i in 0 until playList.count()) {
                mediaController.get().addMediaItem(MediaItem.fromUri(Uri.parse(playList[i].path)))
            }
            mediaController.get().prepare()
        }) {
            Text("Prepare")
        }

        Button(onClick = {
//            viewmodel.playSong(playList[playingSongIndex.intValue])
            mediaController.get().play()
        }) {
            Text("Play")
        }

        Button(onClick = {
            println(mediaController.get().currentMediaItem?.mediaMetadata?.title)
            mediaController.get().pause()
        }) {
            Text("Pause")
        }

        Button(onClick = {
            mediaController.get().seekToPrevious()
        }) {
            Text("Previous")
        }

        Button(onClick = {
            mediaController.get().seekToNext()
        }) {
            Text("Next")
        }

        if (metadata.value != null) {
            Text(metadata.value?.title.toString())
            Text(metadata.value?.artist.toString())
            Text(metadata.value?.albumTitle.toString())
        }


        DisposableEffect(Unit) {
            onDispose {
//                exoPlayer.release()
                mediaController.cancel(true)
            }
        }
    }
}