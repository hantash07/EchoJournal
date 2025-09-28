package com.hantash.echojournal.echo.presentation.echo

interface EchoEvent { // These are the events that are initiated from viewmodel and send to UI
    data object RequestAudioPermission: EchoEvent
    data object RecordingTooShort: EchoEvent
    data object OnDoneRecording: EchoEvent
}