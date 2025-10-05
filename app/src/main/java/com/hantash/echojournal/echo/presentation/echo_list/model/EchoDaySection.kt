package com.hantash.echojournal.echo.presentation.echo_list.model

import com.hantash.echojournal.core.presentation.util.UiText
import com.hantash.echojournal.echo.presentation.model.EchoUi

data class EchoDaySection(
    val dateHeader: UiText,
    val echos: List<EchoUi>
)
