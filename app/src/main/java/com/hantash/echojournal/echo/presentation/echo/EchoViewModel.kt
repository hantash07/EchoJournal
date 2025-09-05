package com.hantash.echojournal.echo.presentation.echo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class EchoViewModel: ViewModel() {
    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(EchoState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = EchoState()
        )

    fun onAction(action: EchoAction) {
        when (action) {
            EchoAction.OnFabClick -> {}
            EchoAction.OnFabLongClick -> {}
            EchoAction.OnMoodChipClipClick -> {}
            is EchoAction.OnRemoveFilters -> {}
            EchoAction.OnTopicChipClick -> {}
            EchoAction.OnSettingsClick -> {}
        }
    }

}