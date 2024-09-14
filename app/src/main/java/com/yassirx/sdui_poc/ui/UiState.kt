package com.yassirx.sdui_poc.ui

sealed class UiState {
    object Idle: UiState()
    object Loading: UiState()
    data class Success(val data: Any? = null): UiState()
    data class Error(val message: String? = null): UiState()
    data class Fail(val message: String? = null): UiState()
}