package com.hantash.echojournal.echo.presentation.echo_list

import com.hantash.echojournal.echo.domain.recording.RecordingDetail

interface EchoEvent { // These are the events that are initiated from viewmodel and send to UI
    data object RequestAudioPermission: EchoEvent
    data object RecordingTooShort: EchoEvent
    data class OnDoneRecording(val recordingDetail: RecordingDetail): EchoEvent
}