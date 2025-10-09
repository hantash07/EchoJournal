package com.hantash.echojournal.echo.presentation.echo_create

import com.hantash.echojournal.core.presentation.designsystem.menu.Selectable
import com.hantash.echojournal.echo.presentation.echo_list.model.PlaybackState
import com.hantash.echojournal.echo.presentation.model.MoodUi
import kotlin.time.Duration

data class EchoCreateState(
    val titleText: String = "",
    val topics: List<String> = listOf(),
    val addTopicText: String = "",
    val noteText: String = "",
    val showMoodSelector: Boolean = true,
    val selectedMood: MoodUi = MoodUi.NEUTRAL,
    val showTopicSuggestions: Boolean = false,
    val mood: MoodUi? = null,
    val searchResults: List<Selectable<String>> = emptyList(),
    val showCreateTopicOption: Boolean = true,
    val canSaveEcho: Boolean = false,
    val playbackAmplitudes: List<Float> = emptyList(),
    val playbackTotalDuration: Duration = Duration.ZERO,
    val playbackState: PlaybackState = PlaybackState.STOPPED,
    val durationPlayed: Duration = Duration.ZERO,
    val showConfirmLeaveDialog: Boolean = false

) {
    val durationPlayedRatio = (durationPlayed / playbackTotalDuration).toFloat()
}
