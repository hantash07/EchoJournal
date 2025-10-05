package com.hantash.echojournal.echo.presentation.echo_create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hantash.echojournal.echo.presentation.model.MoodUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

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
            EchoCreateAction.OnConfirmMood -> onConfirmMood()
            EchoCreateAction.OnCreateNewTopicClick -> TODO()
            EchoCreateAction.OnDismissMoodSelector -> onDismissMoodSelector()
            EchoCreateAction.OnDismissTopicSuggestions -> TODO()
            is EchoCreateAction.OnMoodClick -> onMoodClick(action.moodUi)
            is EchoCreateAction.OnNoteTextChange -> TODO()
            EchoCreateAction.OnPauseAudioClick -> TODO()
            EchoCreateAction.OnPlayAudioClick -> TODO()
            is EchoCreateAction.OnRemoveTopicClick -> TODO()
            EchoCreateAction.OnSaveClick -> TODO()
            EchoCreateAction.OnSelectMoodClick -> onSelectMoodClick()
            is EchoCreateAction.OnTitleTextChange -> TODO()
            is EchoCreateAction.OnTopicClick -> TODO()
            is EchoCreateAction.OnTrackSizeAvailable -> TODO()
        }
    }

    private fun onConfirmMood() {
        _state.update { it.copy(
            mood = it.selectedMood,
            canSaveEcho = it.titleText.isNotBlank(),
            showMoodSelector = false
        ) }
    }

    private fun onDismissMoodSelector() {
        _state.update { it.copy(
            showMoodSelector = false
        ) }
    }

    private fun onSelectMoodClick() {
        _state.update { it.copy(
            showMoodSelector = true
        ) }
    }

    private fun onMoodClick(mood: MoodUi) {
        _state.update { it.copy(
            selectedMood = mood
        ) }
    }
}