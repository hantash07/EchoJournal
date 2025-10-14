@file:OptIn(FlowPreview::class)

package com.hantash.echojournal.echo.presentation.echo_create

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.hantash.echojournal.app.navigation.NavigationRoute
import com.hantash.echojournal.core.presentation.designsystem.menu.Selectable.Companion.asUnselectedItems
import com.hantash.echojournal.echo.domain.echo.Echo
import com.hantash.echojournal.echo.domain.echo.EchoDataSource
import com.hantash.echojournal.echo.domain.echo.Mood
import com.hantash.echojournal.echo.domain.recording.RecordingStorage
import com.hantash.echojournal.echo.domain.settings.SettingsPreferences
import com.hantash.echojournal.echo.presentation.echo_list.model.PlaybackState
import com.hantash.echojournal.echo.presentation.echo_list.model.TrackSizeInfo
import com.hantash.echojournal.echo.presentation.model.MoodUi
import com.hantash.echojournal.echo.presentation.util.AmplitudeNormalizer
import com.hantash.echojournal.echo.presentation.util.toRecordingDetail
import com.plcoding.echojournal.echos.domain.audio.AudioPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import kotlin.time.Duration

class EchoCreateViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val recordingStorage: RecordingStorage,
    private val audioPlayer: AudioPlayer,
    private val echoDataSource: EchoDataSource,
    private val settingsPreferences: SettingsPreferences
): ViewModel() {
    private var hasLoadedInitialData = false

    private val route = savedStateHandle.toRoute<NavigationRoute.CreateEchoScreen>()
    private val recordingDetail = route.toRecordingDetail()

    private val _state = MutableStateFlow(EchoCreateState(
        playbackTotalDuration = recordingDetail.duration,
        titleText = savedStateHandle["titleText"] ?: "",
        noteText = savedStateHandle["noteText"] ?: "",
        topics = savedStateHandle.get<String>("topics")?.split(",") ?: emptyList(),
        mood = savedStateHandle.get<String>("mood")?.let {
            MoodUi.valueOf(it)
        },
        showMoodSelector = savedStateHandle.get<String>("mood") == null,
        canSaveEcho = savedStateHandle.get<Boolean>("canSaveEcho") == true
    ))
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                fetchDefaultSettings()
                observeAddTopicTextChange()
                hasLoadedInitialData = true
            }
        }
        // By doing this we can save the data and restore it during process kill when app is in background
        .onEach { state ->
            savedStateHandle["titleText"] = state.titleText
            savedStateHandle["noteText"] = state.noteText
            savedStateHandle["topics"] = state.topics.joinToString(",")
            savedStateHandle["mood"] = state.mood?.name
            savedStateHandle["canSaveEcho"] = state.canSaveEcho
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = EchoCreateState()
        )

    private val eventChannel = Channel<EchoCreateEvent>()
    val events = eventChannel.receiveAsFlow()

    private var durationJob: Job? = null

    fun onAction(action: EchoCreateAction) {
        when (action) {
            is EchoCreateAction.OnAddTopicTextChange -> onAddTopicTextChange(action.text)
            EchoCreateAction.OnConfirmMood -> onConfirmMood()
            EchoCreateAction.OnDismissMoodSelector -> onDismissMoodSelector()
            EchoCreateAction.OnDismissTopicSuggestions -> onDismissTopicSuggestions()
            is EchoCreateAction.OnMoodClick -> onMoodClick(action.moodUi)
            is EchoCreateAction.OnNoteTextChange -> onNoteTextChange(action.text)
            EchoCreateAction.OnPauseAudioClick -> audioPlayer.pause()
            EchoCreateAction.OnPlayAudioClick -> onPlayAudioClick()
            is EchoCreateAction.OnRemoveTopicClick -> onRemoveTopicClick(action.topic)
            EchoCreateAction.OnSaveClick -> onSaveClick()
            EchoCreateAction.OnSelectMoodClick -> onSelectMoodClick()
            is EchoCreateAction.OnTitleTextChange -> onTitleTextChange(action.text)
            is EchoCreateAction.OnTopicClick -> onTopicClick(action.topic)
            is EchoCreateAction.OnTrackSizeAvailable -> onTrackSizeAvailable(action.trackSizeInfo)
            EchoCreateAction.OnDismissConfirmLeaveDialog -> onDismissConfirmLeaveDialog()
            EchoCreateAction.OnCancelClick, EchoCreateAction.OnNavigateBackClick, EchoCreateAction.OnGoBack -> onShowConfirmLeaveDialog()
        }
    }

    private fun fetchDefaultSettings() {
        settingsPreferences
            .observeDefaultMood()
            .take(1)
            .onEach { defaultMood ->
                _state.update { it.copy(
                    selectedMood = MoodUi.valueOf(defaultMood.name)
                ) }
            }
            .launchIn(viewModelScope)

        settingsPreferences
            .observeDefaultTopics()
            .take(1)
            .onEach { defaultTopics ->
                _state.update { it.copy(
                    topics = defaultTopics
                ) }
            }
            .launchIn(viewModelScope)
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

    private fun onPlayAudioClick() {
        if(state.value.playbackState == PlaybackState.PAUSED) {
            audioPlayer.resume()
        } else {
            audioPlayer.play(
                filePath = recordingDetail.filePath ?: throw IllegalArgumentException(
                    "File path can't be null"
                ),
                onComplete = {
                    _state.update { it.copy(
                        playbackState = PlaybackState.STOPPED,
                        durationPlayed = Duration.ZERO
                    ) }
                }
            )

            durationJob = audioPlayer
                .activeTrack
                .filterNotNull()
                .onEach { track ->
                    _state.update { it.copy(
                        playbackState = if(track.isPlaying) PlaybackState.PLAYING else PlaybackState.PAUSED,
                        durationPlayed = track.durationPlayed
                    ) }
                }
                .launchIn(viewModelScope)
        }
    }

    private fun onTrackSizeAvailable(trackSizeInfo: TrackSizeInfo) {
        viewModelScope.launch(Dispatchers.Default) {
            val finalAmplitudes = AmplitudeNormalizer.normalize(
                sourceAmplitudes = recordingDetail.amplitudes,
                trackWidth = trackSizeInfo.trackWidth,
                barWidth = trackSizeInfo.barWidth,
                spacing = trackSizeInfo.spacing
            )

            _state.update { it.copy(
                playbackAmplitudes = finalAmplitudes
            ) }
        }
    }

    private fun onSaveClick() {
        if(recordingDetail.filePath == null || !state.value.canSaveEcho) {
            return
        }

        viewModelScope.launch {
            val savedFilePath = recordingStorage.savePersistently(
                tempFilePath = recordingDetail.filePath
            )
            if(savedFilePath == null) {
                eventChannel.send(EchoCreateEvent.FailedToSaveFile)
                return@launch
            }

            val currentState = _state.value
            val echo = Echo(
                mood = currentState.mood?.let {
                    Mood.valueOf(it.name)
                } ?: throw IllegalStateException("Mood must be set before saving echo."),
                title = currentState.titleText.trim(),
                note = currentState.noteText.ifBlank { null },
                topics = currentState.topics,
                audioFilePath = savedFilePath,
                audioPlaybackLength = currentState.playbackTotalDuration,
                audioAmplitudes = recordingDetail.amplitudes,
                recordedAt = Instant.now()
            )

            echoDataSource.insertEcho(echo)
            eventChannel.send(EchoCreateEvent.EchoSuccessfullySaved)
        }
    }

    private fun onTitleTextChange(text: String) {
        _state.update { it.copy(
            titleText = text,
            canSaveEcho = text.isNotBlank() && it.mood != null
        ) }
    }

    private fun onNoteTextChange(text: String) {
        _state.update { it.copy(
            noteText = text
        ) }
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