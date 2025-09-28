package com.hantash.echojournal.echo.presentation.echo

import com.hantash.echojournal.R
import com.hantash.echojournal.core.presentation.designsystem.menu.Selectable
import com.hantash.echojournal.core.presentation.designsystem.menu.Selectable.Companion.asUnselectedItems
import com.hantash.echojournal.core.presentation.util.UiText
import com.hantash.echojournal.echo.presentation.echo.model.AudioCaptureMethod
import com.hantash.echojournal.echo.presentation.echo.model.EchoDaySection
import com.hantash.echojournal.echo.presentation.echo.model.EchoFilterChip
import com.hantash.echojournal.echo.presentation.echo.model.MoodChipContent
import com.hantash.echojournal.echo.presentation.echo.model.RecordingState
import com.hantash.echojournal.echo.presentation.model.EchoUi
import com.hantash.echojournal.echo.presentation.model.MoodUi
import java.util.Locale
import kotlin.math.roundToInt
import kotlin.time.Duration

data class EchoState(
    val echos: Map<UiText, List<EchoUi>> = emptyMap(),
    val hasEchoRecorded: Boolean = false,
    val hasActiveTopicFilters: Boolean = false,
    val hasActiveMoodFilters: Boolean = false,
    val isLoadingData: Boolean = false,
    val recordingState: RecordingState = RecordingState.NOT_RECORDING,
    val moods: List<Selectable<MoodUi>> = emptyList(),
    val topics: List<Selectable<String>> = listOf("Love", "Happy", "Work").asUnselectedItems(),
    val moodChipContent: MoodChipContent = MoodChipContent(),
    val topicChipTitle: UiText = UiText.StringResource(R.string.add_topic),
    val selectedEchoFilterChip: EchoFilterChip? = null,
    val currentAudioCaptureMethod: AudioCaptureMethod? = null,
    val recordingElapsedDuration: Duration = Duration.ZERO
) {
    val echoDaySelections = echos
        .toList()
        .map { (dateHeader, echos) ->
            EchoDaySection(dateHeader, echos)
        }

    val formattedRecordDuration: String
        get() {
            val minutes = (recordingElapsedDuration.inWholeMinutes % 60).toInt()
            val seconds = (recordingElapsedDuration.inWholeSeconds % 60).toInt()
            val milliseconds = ((recordingElapsedDuration.inWholeMicroseconds % 1000) / 10.0 ).roundToInt()

            return String.format(
                locale = Locale.US,
                format = "%02d:%02d:%02d",
                minutes, seconds, milliseconds
            )
        }
}