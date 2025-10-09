package com.hantash.echojournal.echo.presentation.preview

import com.hantash.echojournal.echo.presentation.echo_list.model.PlaybackState
import com.hantash.echojournal.echo.presentation.model.EchoUi
import com.hantash.echojournal.echo.presentation.model.MoodUi
import java.time.Instant
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

data object PreviewModel {
    val echoUi = EchoUi(
        id = 0,
        title = "My Audio Memo",
        note = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
        moodUi = MoodUi.NEUTRAL,
        audioFilePath = "",
        topics = listOf("Love", "Work"),
        amplitudes = (1..30).map { Random.nextFloat() },
        playbackState = PlaybackState.PAUSED,
        playbackCurrentDuration = 120.seconds,
        playbackTotalDuration = 250.seconds,
        recordedAt = Instant.now()
    )
}