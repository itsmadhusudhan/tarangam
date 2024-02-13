package com.shreekaram.tarangam.core

sealed class ScreenUIState<out T> {
    data object Loading : ScreenUIState<Nothing>()
    data class Success<out T>(val data: T) : ScreenUIState<T>()
    data object Error : ScreenUIState<Nothing>()
}
