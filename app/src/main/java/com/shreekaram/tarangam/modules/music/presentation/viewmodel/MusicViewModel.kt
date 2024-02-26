package com.shreekaram.tarangam.modules.music.presentation.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shreekaram.tarangam.modules.music.domain.IMusicRepository
import com.shreekaram.tarangam.modules.music.domain.Music
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicViewModel @Inject constructor(private val repository: IMusicRepository) : ViewModel() {
    val musicList = MutableStateFlow<MutableList<Music>>(mutableStateListOf())

    fun loadMusic() {
        viewModelScope.launch {
            repository.getMusic().collectLatest { music ->
                val currentList = musicList.value.toMutableList()
                currentList.add(music)
                musicList.value.add(music)
            }
        }
    }
}