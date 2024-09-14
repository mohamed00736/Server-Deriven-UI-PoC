package com.arbin.arbincommon.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class BaseViewModel<T>: ViewModel() {
    val _uiState: MutableStateFlow<T?> = MutableStateFlow(null)
    val uiState: StateFlow<T?> get() = _uiState
}