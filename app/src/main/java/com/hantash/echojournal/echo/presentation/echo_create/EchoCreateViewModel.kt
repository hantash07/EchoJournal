@file:OptIn(FlowPreview::class)

package com.hantash.echojournal.echo.presentation.echo_create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hantash.echojournal.core.presentation.designsystem.menu.Selectable.Companion.asUnselectedItems
import com.hantash.echojournal.echo.presentation.model.MoodUi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlin.text.contains

class EchoCreateViewModel: ViewModel() {
    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(EchoCreateState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                observeAddTopicTextChange()
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
            is EchoCreateAction.OnAddTopicTextChange -> onAddTopicTextChange(action.text)
            EchoCreateAction.OnConfirmMood -> onConfirmMood()
            EchoCreateAction.OnDismissMoodSelector -> onDismissMoodSelector()
            EchoCreateAction.OnDismissTopicSuggestions -> onDismissTopicSuggestions()
            is EchoCreateAction.OnMoodClick -> onMoodClick(action.moodUi)
            is EchoCreateAction.OnNoteTextChange -> TODO()
            EchoCreateAction.OnPauseAudioClick -> TODO()
            EchoCreateAction.OnPlayAudioClick -> TODO()
            is EchoCreateAction.OnRemoveTopicClick -> onRemoveTopicClick(action.topic)
            EchoCreateAction.OnSaveClick -> TODO()
            EchoCreateAction.OnSelectMoodClick -> onSelectMoodClick()
            is EchoCreateAction.OnTitleTextChange -> TODO()
            is EchoCreateAction.OnTopicClick -> onTopicClick(action.topic)
            is EchoCreateAction.OnTrackSizeAvailable -> TODO()
            EchoCreateAction.OnDismissConfirmLeaveDialog -> onDismissConfirmLeaveDialog()
            EchoCreateAction.OnCancelClick, EchoCreateAction.OnNavigateBackClick, EchoCreateAction.OnGoBack -> onShowConfirmLeaveDialog()
        }
    }

    private fun observeAddTopicTextChange() {
        state
            .map { it.addTopicText }
            .distinctUntilChanged()
            .debounce(300)
            .onEach { query ->
                _state.update { it.copy(
                    showTopicSuggestions = query.isNotBlank() && query.trim() !in it.topics,
                    searchResults = listOf(
                        "hello",
                        "helloworld",
                    ).asUnselectedItems()
                ) }
            }
            .launchIn(viewModelScope)
    }

    private fun onShowConfirmLeaveDialog() {
        _state.update { it.copy(
            showConfirmLeaveDialog = true
        ) }
    }

    private fun onDismissConfirmLeaveDialog() {
        _state.update { it.copy(
            showConfirmLeaveDialog = false
        ) }
    }

    private fun onAddTopicTextChange(text: String) {
        _state.update { it.copy(
            addTopicText = text.filter { text -> text.isLetterOrDigit() }
        ) }
    }

    private fun onTopicClick(topic: String) {
        _state.update { it.copy(
            topics = (it.topics + topic).distinct(),
            addTopicText = ""
        ) }
    }

    private fun onRemoveTopicClick(topic: String) {
        _state.update { it.copy(
            topics = it.topics - topic
        ) }
    }

    private fun onDismissTopicSuggestions() {
        _state.update { it.copy(
            showTopicSuggestions = false
        ) }
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