package com.hantash.echojournal.app.navigation

import kotlinx.serialization.Serializable

sealed interface NavigationRoute {
    @Serializable
    data class EchoScreen(
        val startRecording: Boolean
    ): NavigationRoute

    @Serializable
    data class CreateEchoScreen(
        val recordingPath: String,
        val duration: Long,
        val amplitudes: String
    ): NavigationRoute

    @Serializable
    data object SettingsScreen: NavigationRoute
}