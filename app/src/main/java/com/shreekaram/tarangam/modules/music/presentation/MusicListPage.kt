package com.shreekaram.tarangam.modules.music.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import com.shreekaram.tarangam.modules.music.presentation.viewmodel.MusicViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MusicListPage(padding: PaddingValues) {
    val viewModel = hiltViewModel<MusicViewModel>()

    val musicList = viewModel.musicList.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.loadMusic()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Tarangam")
                },
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(bottom = innerPadding.calculateBottomPadding() + padding.calculateBottomPadding())
        ) {
            itemsIndexed(musicList) { index, music ->
                val hours = (music.duration / 3600000).toInt()
                val minutes = ((music.duration % 3600000) / 60000).toInt()
                val seconds = ((music.duration % 60000) / 1000).toInt()
                val duration = if (hours > 0) {
                    "$hours:${minutes.toString().padStart(2, '0')}:${
                        seconds.toString().padStart(2, '0')
                    }"
                } else {
                    "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
                }
                ListItem(
                    headlineContent = {
                        Text(
                            text = music.title + index.toString(),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    },
                    supportingContent = { Text(text = music.album) },
                    trailingContent = { Text(text = duration) },
                    modifier = Modifier.clickable { }
                )
            }
        }
    }
}