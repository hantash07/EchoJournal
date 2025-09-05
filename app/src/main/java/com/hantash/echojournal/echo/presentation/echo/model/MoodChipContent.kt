package com.hantash.echojournal.echo.presentation.echo.model

import com.hantash.echojournal.R
import com.hantash.echojournal.core.presentation.util.UiText

data class MoodChipContent(
    val iconsRes: List<Int> = emptyList(),
    val title: UiText = UiText.StringResource(R.string.all_moods)
)
