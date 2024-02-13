package com.shreekaram.tarangam.navigation

sealed class Routes(var id: String, var title: String) {
    data object Home : Routes("/", "Home")
    data object Search : Routes("search", "Search")
    data object MusicList : Routes("music-list", "MusicList")
}