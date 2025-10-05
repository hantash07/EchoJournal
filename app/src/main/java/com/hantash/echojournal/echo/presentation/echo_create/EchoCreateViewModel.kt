package com.hantash.echojournal.echo.presentation.echo_create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class EchoCreateViewModel: ViewModel() {
    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(EchoCreateState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = EchoCreateState()
        )

    fun onAction(action: EchoCreateAction) {
        when (action) {
            EchoCreateAction.OnNavigateBackClick -> {}
            is EchoCreateAction.OnAddTopicTextChange -> TODO()
            EchoCreateAction.OnCancelClick -> TODO()
            EchoCreateAction.OnConfirmMood -> TODO()
            EchoCreateAction.OnCreateNewTopicClick -> TODO()
            EchoCreateAction.OnDismissMoodSelector -> TODO()
            EchoCreateAction.OnDismissTopicSuggestions -> TODO()
            is EchoCreateAction.OnMoodClick -> TODO()
            is EchoCreateAction.OnNoteTextChange -> TODO()
            EchoCreateAction.OnPauseAudioClick -> TODO()
            EchoCreateAction.OnPlayAudioClick -> TODO()
            is EchoCreateAction.OnRemoveTopicClick -> TODO()
            EchoCreateAction.OnSaveClick -> TODO()
            EchoCreateAction.OnSelectMoodClick -> TODO()
            is EchoCreateAction.OnTitleTextChange -> TODO()
            is EchoCreateAction.OnTopicClick -> TODO()
            is EchoCreateAction.OnTrackSizeAvailable -> TODO()
        }
    }
}