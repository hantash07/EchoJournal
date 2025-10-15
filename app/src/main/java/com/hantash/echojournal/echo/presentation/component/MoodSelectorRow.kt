package com.hantash.echojournal.echo.presentation.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hantash.echojournal.echo.presentation.model.MoodUi

@Composable
fun MoodSelectorRow(
    modifier: Modifier = Modifier,
    selectedMood: MoodUi?,
    onMoodClick: (MoodUi) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MoodUi.entries.forEach { mood ->
            MoodItem(
                selected = mood == selectedMood,
                mood = mood,
                onClick = { onMoodClick(mood) },
            )
        }
    }
}