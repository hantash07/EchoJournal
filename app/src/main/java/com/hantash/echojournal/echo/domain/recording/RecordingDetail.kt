package com.hantash.echojournal.echo.domain.recording

import kotlin.time.Duration

data class RecordingDetail(
    val duration: Duration = Duration.ZERO,
    val amplitudes: List<Float> = emptyList(),
    val filePath: String? = null
)
