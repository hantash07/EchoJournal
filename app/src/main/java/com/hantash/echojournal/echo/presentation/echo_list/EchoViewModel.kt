package com.hantash.echojournal.echo.presentation.echo_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hantash.echojournal.R
import com.hantash.echojournal.core.presentation.designsystem.menu.Selectable
import com.hantash.echojournal.core.presentation.util.UiText
import com.hantash.echojournal.echo.domain.echo.Echo
import com.hantash.echojournal.echo.domain.echo.EchoDataSource
import com.hantash.echojournal.echo.domain.recording.VoiceRecorder
import com.hantash.echojournal.echo.presentation.echo_list.model.AudioCaptureMethod
import com.hantash.echojournal.echo.presentation.echo_list.model.EchoFilterChip
import com.hantash.echojournal.echo.presentation.echo_list.model.MoodChipContent
import com.hantash.echojournal.echo.presentation.echo_list.model.PlaybackState
import com.hantash.echojournal.echo.presentation.echo_list.model.RecordingState
import com.hantash.echojournal.echo.presentation.echo_list.model.TrackSizeInfo
import com.hantash.echojournal.echo.presentation.model.EchoUi
import com.hantash.echojournal.echo.presentation.model.MoodUi
import com.hantash.echojournal.echo.presentation.util.AmplitudeNormalizer
import com.hantash.echojournal.echo.presentation.util.toEchoUi
import com.plcoding.echojournal.echos.domain.audio.AudioPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class EchoViewModel(
    private val voiceRecorder: VoiceRecorder,
    private val audioPlayer: AudioPlayer,
    private val echoDataSource: EchoDataSource
): ViewModel() {

    companion object {
        private val MIN_RECORD_DURATION = 1.5.seconds
    }

    private var hasLoadedInitialData = false

    //NOTE: Difference b/w State, Event and Action in detail.
    private val _state = MutableStateFlow(EchoState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeFilters() //NOTE: Not a better approach to load initial data. RND on recommended approach.
                observeEchos()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = EchoState()
        )

    private val _eventChannel = Channel<EchoEvent>()
    val events = _eventChannel.receiveAsFlow()

    private val playingEchoId = MutableStateFlow<Int?>(null)
    private val selectedMoodFilters = MutableStateFlow<List<MoodUi>>(emptyList())
    private val selectedTopicFilters = MutableStateFlow<List<String>>(emptyList())
    private val audioTrackSizeInfo = MutableStateFlow<TrackSizeInfo?>(null)

    private val filteredEchos = echoDataSource
        .observeEchos()
        .filterByMoodAndTopics()
        .onEach { echos ->
            _state.update { it.copy(
                hasEchoRecorded = echos.isNotEmpty(),
                isLoadingData = false
            ) }
        }
        .combine(audioTrackSizeInfo) { echos, trackSizeInfo ->
            if(trackSizeInfo != null) {
                echos.map { echo ->
                    echo.copy(
                        audioAmplitudes = AmplitudeNormalizer.normalize(
                            sourceAmplitudes = echo.audioAmplitudes,
                            trackWidth = trackSizeInfo.trackWidth,
                            barWidth = trackSizeInfo.barWidth,
                            spacing = trackSizeInfo.spacing
                        )
                    )
                }
            } else echos
        }
        .flowOn(Dispatchers.Default)

    fun onAction(action: EchoAction) {
        when (action) {

            EchoAction.OnFabClick -> {
                Timber.d("OnFabClick")
                _state.update { it.copy(
                    currentAudioCaptureMethod = AudioCaptureMethod.STANDARD
                ) }
                requestAudioPermission()
            }

            EchoAction.OnFabLongClick -> {
                Timber.d("OnFabLongClick")
                _state.update { it.copy(
                    currentAudioCaptureMethod = AudioCaptureMethod.QUICK
                ) }
                requestAudioPermission()
            }

            is EchoAction.OnAudioPermissionGranted -> {
                val audioCaptureMethod = _state.value.currentAudioCaptureMethod
                audioCaptureMethod?.let {
                    Timber.d("Selected Method: ${audioCaptureMethod.name}")
                    startRecording(it)
                }
            }

            EchoAction.OnSettingsClick -> {
                Timber.d("OnSettingsClick")
            }

            EchoAction.OnMoodClipClick -> {
                _state.update { it.copy(
                    selectedEchoFilterChip = EchoFilterChip.MOODS
                ) }
            }
            EchoAction.OnDismissMoodDropDown -> {
                _state.update { it.copy(
                    selectedEchoFilterChip = null
                ) }
            }
            is EchoAction.OnFilterByMoodClick -> {
                toggleMoodFilter(action.moodUi)
            }

            EchoAction.OnTopicChipClick -> {
                _state.update { it.copy(
                    selectedEchoFilterChip = EchoFilterChip.TOPICS
                ) }
            }
            EchoAction.OnDismissTopicDropDown -> {
                _state.update { it.copy(
                    selectedEchoFilterChip = null
                ) }
            }
            is EchoAction.OnFilterByTopicClick -> {
                toggleTopicFilter(action.topic)
            }

            is EchoAction.OnRemoveFilters -> {
                when(action.filterType) {
                    EchoFilterChip.MOODS -> selectedMoodFilters.update { emptyList() }
                    EchoFilterChip.TOPICS -> selectedTopicFilters.update { emptyList() }
                }
            }

            EchoAction.OnPauseAudioClick -> audioPlayer.pause()
            is EchoAction.OnPlayEchoClick -> onPlayEchoClick(action.echoId)
            is EchoAction.OnTrackSizeAvailable -> {
                audioTrackSizeInfo.update { action.trackSizeInfo }
            }

            EchoAction.OnCancelRecording -> cancelRecording()
            EchoAction.OnPauseRecordingClick -> pauseRecording()
            EchoAction.OnCompleteRecording -> stopRecording()
            EchoAction.OnResumeRecordingClick -> resumeRecording()
        }
    }

    private fun observeEchos() {
        combine(
            filteredEchos,
            playingEchoId,
            audioPlayer.activeTrack
        ) { echos, playingEchoId, activeTrack ->
            if(playingEchoId == null || activeTrack == null) {
                return@combine echos.map { it.toEchoUi() }
            }

            echos.map { echo ->
                if(echo.id == playingEchoId) {
                    echo.toEchoUi(
                        currentPlaybackDuration = activeTrack.durationPlayed,
                        playbackState = if(activeTrack.isPlaying) PlaybackState.PLAYING else PlaybackState.PAUSED
                    )
                } else echo.toEchoUi()
            }
        }
            .groupByRelativeDate()
            .onEach { groupedEchos ->
                _state.update { it.copy(
                    echos = groupedEchos
                ) }
            }
            .flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)
    }

    private fun onPlayEchoClick(echoId: Int) {
        val selectedEcho = state.value.echos.values.flatten().first { it.id == echoId }
        val activeTrack = audioPlayer.activeTrack.value
        val isNewEcho = playingEchoId.value != echoId
        val isSameEchoIsPlayingFromBeginning = echoId == playingEchoId.value && activeTrack != null
                && activeTrack.durationPlayed == Duration.ZERO

        when {
            isNewEcho || isSameEchoIsPlayingFromBeginning -> {
                playingEchoId.update { echoId }
                audioPlayer.stop()
                audioPlayer.play(
                    filePath = selectedEcho.audioFilePath,
                    onComplete = ::completePlayback
                )
            }
            else -> audioPlayer.resume()
        }
    }

    private fun completePlayback() {
        _state.update { it.copy(
            echos = it.echos.mapValues { (_, echos) ->
                echos.map { echo ->
                    echo.copy(
                        playbackCurrentDuration = Duration.ZERO
                    )
                }
            }
        ) }
        playingEchoId.update { null }
    }

    private fun toggleMoodFilter(moodUi: MoodUi) {
        selectedMoodFilters.update { selectedMoods ->
            if (moodUi in selectedMoods) {
                selectedMoods - moodUi
            } else {
                selectedMoods + moodUi
            }
        }
    }

    private fun toggleTopicFilter(topic: String) {
        selectedTopicFilters.update { selectedTopics ->
            if (topic in selectedTopics) {
                selectedTopics - topic
            } else {
                selectedTopics + topic
            }
        }
    }

    private fun observeFilters() {
        combine(
            echoDataSource.observeTopics(),
            selectedTopicFilters,
            selectedMoodFilters
        ) { allTopics, selectedTopics, selectedMoods ->
            _state.update { it.copy(
                topicChipTitle = selectedTopics.deriveTopicsToText(),
                hasActiveTopicFilters = selectedTopics.isNotEmpty(),
                topics = allTopics.map { topic ->
                    Selectable(
                        item = topic,
                        selected = selectedTopics.contains(topic)
                    )
                },
                moodChipContent = selectedMoods.asMoodChipContent(),
                hasActiveMoodFilters = selectedMoods.isNotEmpty(),
                moods = MoodUi.entries.map { mood ->
                    Selectable(
                        item = mood,
                        selected = selectedMoods.contains(mood)
                    )
                },
            ) }
        }.launchIn(viewModelScope)
    }

    private fun List<String>.deriveTopicsToText(): UiText {
        return when(size) {
            0 -> UiText.StringResource(R.string.all_topics)
            1 -> UiText.Dynamic(this.first())
            2 -> UiText.Dynamic("${this.first()}, ${this.last()}")
            else -> {
                val extraElementCount = size - 2
                UiText.Dynamic("${this.first()}, ${this[1]} +$extraElementCount")
            }
        }
    }

    private fun List<MoodUi>.asMoodChipContent(): MoodChipContent {
        if (this.isEmpty()) {
            return MoodChipContent()
        }

        val icons = this.map { it.iconSet.fill }
        val names = this.map { it.title }

        return when(size) {
            1 -> MoodChipContent(
                iconsRes = icons,
                title = names.first()
            )
            2 -> MoodChipContent(
                iconsRes = icons,
                title = UiText.Combined(
                    format = "%s, %s",
                    uiTexts = names.toTypedArray()
                )
            )
            else -> {
                val extraElementCount = size - 2
                MoodChipContent(
                    iconsRes = icons,
                    title = UiText.Combined(
                        format = "%s, %s +$extraElementCount",
                        uiTexts = names.toTypedArray()
                    )
                )
            }
        }
    }

    private fun requestAudioPermission() = viewModelScope.launch {
        _eventChannel.send(EchoEvent.RequestAudioPermission)
    }

    private fun startRecording(captureMethod: AudioCaptureMethod) {
        _state.update { it.copy(
            recordingState = when(captureMethod) {
                AudioCaptureMethod.STANDARD -> RecordingState.NORMAL_CAPTURE
                AudioCaptureMethod.QUICK -> RecordingState.QUICK_CAPTURE
            }
        ) }
        voiceRecorder.start()

        if (captureMethod == AudioCaptureMethod.STANDARD) {
            voiceRecorder
                .recordingDetail
                .distinctUntilChangedBy { it.duration }
                .map { it.duration }
                .onEach { duration ->
                    _state.update { it.copy(
                        recordingElapsedDuration = duration
                    ) }
                }
                .launchIn(viewModelScope)
        }
    }

    private fun pauseRecording() {
        voiceRecorder.pause()
        _state.update { it.copy(
            recordingState = RecordingState.PAUSED
        ) }
    }

    private fun resumeRecording() {
        voiceRecorder.resume()
        _state.update { it.copy(
            recordingState = RecordingState.NORMAL_CAPTURE
        ) }
    }

    private fun cancelRecording() {
        voiceRecorder.cancel()
        _state.update { it.copy(
            recordingState = RecordingState.NOT_RECORDING,
            currentAudioCaptureMethod = null
        ) }
    }

    private fun stopRecording() {
        voiceRecorder.stop()
        _state.update { it.copy(
            recordingState = RecordingState.NOT_RECORDING
        ) }

        val recordingDetail = voiceRecorder.recordingDetail.value
        viewModelScope.launch {
            if (recordingDetail.duration < MIN_RECORD_DURATION) {
                _eventChannel.send(EchoEvent.RecordingTooShort)
            } else {
                _eventChannel.send(EchoEvent.OnDoneRecording(recordingDetail))
            }
        }
    }

    private fun Flow<List<EchoUi>>.groupByRelativeDate(): Flow<Map<UiText, List<EchoUi>>> {
        val formatter = DateTimeFormatter.ofPattern("dd MMM")
        val today = LocalDate.now()
        return map { echos ->
            echos
                .groupBy { echo ->
                    LocalDate.ofInstant(
                        echo.recordedAt,
                        ZoneId.systemDefault()
                    )
                }
                .mapValues { (_, echos) ->
                    echos.sortedByDescending { it.recordedAt }
                }
                .toSortedMap(compareByDescending { it })
                .mapKeys { (date, _) ->
                    when(date) {
                        today -> UiText.StringResource(R.string.today)
                        today.minusDays(1) -> UiText.StringResource(R.string.yesterday)
                        else -> UiText.Dynamic(date.format(formatter))
                    }
                }
        }
    }

    private fun Flow<List<Echo>>.filterByMoodAndTopics(): Flow<List<Echo>> {
        return combine(
            this,
            selectedMoodFilters,
            selectedTopicFilters
        ) { echos, moodFilters, topicFilters ->
            echos.filter { echo ->
                val matchesMoodFilter = moodFilters
                    .takeIf { it.isNotEmpty() }
                    ?.any { it.name == echo.mood.name }
                    ?: true
                val matchesTopicFilter = topicFilters
                    .takeIf { it.isNotEmpty() }
                    ?.any { it in echo.topics }
                    ?: true

                matchesMoodFilter && matchesTopicFilter
            }
        }
    }
}