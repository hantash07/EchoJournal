package com.hantash.echojournal.echo.domain.recording

import kotlinx.coroutines.flow.StateFlow

interface VoiceRecorder {
    val recordingDetail: StateFlow<RecordingDetail>
    fun start()
    fun pause()
    fun stop()
    fun resume()
    fun cancel()
}