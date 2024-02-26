package com.shreekaram.tarangam.modules.music.data

import android.content.ContentResolver
import android.provider.MediaStore
import com.shreekaram.tarangam.modules.music.data.utils.MusicHelper
import com.shreekaram.tarangam.modules.music.domain.IMusicRepository
import com.shreekaram.tarangam.modules.music.domain.Music
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class MusicRepository @Inject constructor(
    private val contentResolver: ContentResolver,
    private val helper: MusicHelper
) : IMusicRepository {
    //    create a coroutine scope
    private val repoScope = CoroutineScope(Dispatchers.IO)


    override suspend fun getMusic(): Flow<Music> {
        val channel = Channel<Music>(Channel.UNLIMITED)

        repoScope.launch {
            // Define URI to access all music files
            val musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val folderToExclude = "/storage/emulated/0/Music"
            val selection = "${MediaStore.Audio.Media.DATA} LIKE ?"
            val selectionArgs = arrayOf("$folderToExclude%")
            val projection = arrayOf(
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
            )

            // Query the MediaStore
            val musicCursor =
                contentResolver.query(musicUri, projection, selection, selectionArgs, null)

            // Iterate through the cursor and extract information
            musicCursor?.let { cursor ->
                while (cursor.moveToNext()) {
                    val title =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                    val artist =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                    val filePath =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                    val album =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
                    val duration =
                        cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))

                    val music = Music(
                        title = title,
                        artist = artist,
                        album = album,
                        duration = duration,
                        albumArt = helper.getAlbumArt(File(filePath)),
                        path = filePath
                    )

                    repoScope.launch {
                        channel.send(music)
                    }
                }
                cursor.close()

            }
        }

        return channel.receiveAsFlow()
    }

    override fun dispose() {
        repoScope.cancel()
    }

//    @OptIn(ExperimentalPathApi::class)
//    suspend fun walkMusic(): Flow<Music> {
//        // just take the music directory from the external storage
//        val path =
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).absolutePath
//        val directory = File(path)
//        val filter = MusicFilter()
//
//        val channel = Channel<Music>(Channel.UNLIMITED)
//
//        repoScope.launch {
//            Files.walkFileTree(
//                directory.toPath(),
//                fileVisitor {
//                    onPreVisitDirectory { file, _ ->
//                        if (file.name.contains(".")) {
//                            return@onPreVisitDirectory SKIP_SUBTREE
//                        }
//                        CONTINUE
//                    }
//                    onVisitFile { file, _ ->
//                        if (file.parent.name.contains(".")) {
//                            return@onVisitFile SKIP_SIBLINGS
//                        }
//                        val canAccept = filter.accept(file.toFile())
//
//                        if (canAccept) {
//                            repoScope.launch {
//                                val music = musicHelper.getMusicWithMetadata(file.toFile())
//                                channel.send(music)
//                            }
//                        }
//
//                        CONTINUE
//                    }
//                    onVisitFileFailed { _, _ ->
//                        CONTINUE
//                    }
//                },
//            )
//        }
//
//        return channel.receiveAsFlow()
//    }

}