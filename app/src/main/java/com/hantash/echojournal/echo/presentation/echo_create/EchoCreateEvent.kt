package com.hantash.echojournal.echo.presentation.echo_create

sealed interface EchoCreateEvent {
    data object FailedToSaveFile: EchoCreateEvent
    data object EchoSuccessfullySaved: EchoCreateEvent
}