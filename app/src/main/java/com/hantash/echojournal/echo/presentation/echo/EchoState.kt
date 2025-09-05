package com.hantash.echojournal.echo.presentation.echo

import com.hantash.echojournal.core.presentation.designsystem.menu.Selectable
import com.hantash.echojournal.echo.presentation.model.MoodUi

data class EchoState(
    val hasEchoRecorded: Boolean = false,
    val hasActiveTopicFilters: Boolean = false,
    val hasActiveModeFilters: Boolean = false,
    val isLoadingData: Boolean = false,
    val moods: List<Selectable<MoodUi>> = emptyList(),
    val topics: List<Selectable<String>> = emptyList()
)