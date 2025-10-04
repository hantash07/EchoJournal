package com.hantash.echojournal.echo.presentation.util

import com.hantash.echojournal.app.navigation.NavigationRoute
import com.hantash.echojournal.echo.domain.recording.RecordingDetail
import kotlin.time.Duration.Companion.milliseconds

fun RecordingDetail.toCreateEchoRoute(): NavigationRoute.CreateEcho {
    return NavigationRoute.CreateEcho(
        recordingPath = this.filePath ?: throw IllegalArgumentException(
            "Recording path can't be null."
        ),
        duration = this.duration.inWholeMilliseconds,
        amplitudes = this.amplitudes.joinToString(";")
    )
}

fun NavigationRoute.CreateEcho.toRecordingDetails(): RecordingDetail {
    return RecordingDetail(
        duration = this.duration.milliseconds,
        amplitudes = this.amplitudes.split(";").map { it.toFloat() },
        filePath = recordingPath
    )
}