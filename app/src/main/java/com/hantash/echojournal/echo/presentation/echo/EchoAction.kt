package com.hantash.echojournal.echo.presentation.echo

import com.hantash.echojournal.echo.presentation.echo.model.EchoFilterChip
import com.hantash.echojournal.echo.presentation.model.MoodUi

sealed interface EchoAction {
    data object OnFabClick: EchoAction
    data object OnFabLongClick: EchoAction
    data object OnSettingsClick: EchoAction
    data class OnRemoveFilters(val filterType: EchoFilterChip): EchoAction
    data object OnMoodClipClick: EchoAction
    data object OnDismissMoodDropDown: EchoAction
    data class OnFilterByMoodClick(val moodUi: MoodUi): EchoAction
    data object OnTopicChipClick: EchoAction
    data object OnDismissTopicDropDown: EchoAction
    data class OnFilterByTopicClick(val topic: String): EchoAction
}