package com.hantash.echojournal.echo.presentation.echo

import com.hantash.echojournal.R
import com.hantash.echojournal.core.presentation.designsystem.menu.Selectable
import com.hantash.echojournal.core.presentation.designsystem.menu.Selectable.Companion.asUnselectedItems
import com.hantash.echojournal.core.presentation.util.UiText
import com.hantash.echojournal.echo.presentation.echo.model.EchoFilterChip
import com.hantash.echojournal.echo.presentation.echo.model.MoodChipContent
import com.hantash.echojournal.echo.presentation.model.MoodUi

data class EchoState(
    val hasEchoRecorded: Boolean = false,
    val hasActiveTopicFilters: Boolean = false,
    val hasActiveModeFilters: Boolean = false,
    val isLoadingData: Boolean = false,
    val moods: List<Selectable<MoodUi>> = emptyList(),
    val topics: List<Selectable<String>> = listOf("Love", "Happy", "Work").asUnselectedItems(),
    val moodChipContent: MoodChipContent = MoodChipContent(),
    val topicChipTitle: UiText = UiText.StringResource(R.string.add_topic),
    val selectedEchoFilterChip: EchoFilterChip? = null
)