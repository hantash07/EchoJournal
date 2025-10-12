package com.hantash.echojournal.echo.domain.echo

import java.time.Instant
import kotlin.time.Duration

/*
 New Mood enum class is created as we cannot use MoodUI in this class because MoodUI belongs to Presentation Layer.
 Domain layer cannot have dependency from Presentation layer.
*/

/*
 NOTE: Is it good alternative to place MoodUI inside Core package? By doing this it will be used in this class.
 Can we also create this Echo class inside Core package or common package?
*/

data class Echo(
    val mood: Mood,
    val title: String,
    val note: String?,
    val topics: List<String>,
    val audioFilePath: String,
    val audioPlaybackLength: Duration,
    val audioAmplitudes: List<Float>,
    val recordedAt: Instant,
    val id: Int? = null,
)
