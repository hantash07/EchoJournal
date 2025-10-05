package com.hantash.echojournal.echo.presentation.model

import com.hantash.echojournal.echo.presentation.echo_list.model.PlaybackState
import com.hantash.echojournal.echo.presentation.util.toReadableTime
import kotlin.time.Duration
import java.time.Instant as JavaInstant

data class EchoUi(
    val id: Int,
    val title: String,
    val note: String?,
    val moodUi: MoodUi,
    val topics: List<String>,
    val amplitudes: List<Float>,
    val playbackState: PlaybackState = PlaybackState.STOPPED,
    val playbackCurrentDuration: Duration = Duration.ZERO,
    val playbackTotalDuration: Duration,
    val recordedAt: JavaInstant
) {
    val formattedRecordedAt = recordedAt.toReadableTime()
    val playbackRatio = (playbackCurrentDuration / playbackTotalDuration).toFloat()
}
