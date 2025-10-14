package com.hantash.echojournal.echo.presentation.settings

import com.hantash.echojournal.echo.presentation.model.MoodUi

data class SettingsState(
    val selectedMood: MoodUi? = null,
    val searchText: String = "",
    val topics: List<String> = emptyList(),
    val suggestedTopics: List<String> = emptyList(),
    val showCreateTopicOption: Boolean = false,
    val isTopicTextInputVisible: Boolean = false,
    val isTopicSuggestionsVisible: Boolean = false
)