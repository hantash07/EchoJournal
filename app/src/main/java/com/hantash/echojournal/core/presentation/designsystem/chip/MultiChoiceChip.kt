package com.hantash.echojournal.core.presentation.designsystem.chip

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hantash.echojournal.R
import com.hantash.echojournal.core.presentation.designsystem.theme.EchoJournalTheme

@Composable
fun MultiChoiceChip(
    modifier: Modifier = Modifier,
    displayText: String,
    isClearVisible: Boolean,
    isHighlighted: Boolean,
    isDropDownVisible: Boolean,
    dropDownMenu: @Composable () -> Unit,
    leadingContent: (@Composable () -> Unit)? = null,
    onClearButtonClick: () -> Unit,
    onClick: () -> Unit
) {
    val shape = CircleShape

    val containerColor = if (isHighlighted) {
        MaterialTheme.colorScheme.surface
    } else {
        Color.Transparent
    }

    val borderColor = if (isHighlighted) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.outline
    }

    val shadowModifier = if (isHighlighted) {
        Modifier.shadow(elevation = 4.dp, shape = shape)
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .then(shadowModifier)
            .clip(shape)
            .border(
                width = 0.5.dp,
                color = borderColor,
                shape = shape
            )
            .background(containerColor)
            .clickable(onClick = onClick)
            .animateContentSize()
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            leadingContent?.invoke()

            Text(
                text = displayText,
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondary
            )

            AnimatedVisibility(
                visible = isClearVisible
            ) {
                IconButton(
                    modifier = Modifier
                        .size(16.dp),
                    onClick = onClearButtonClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(R.string.clear_selections),
                        tint = MaterialTheme.colorScheme.secondaryContainer
                    )
                }
            }

        }

        if (isDropDownVisible) {
            dropDownMenu()
        }
    }
}

@Preview
@Composable
fun PreviewMultiChoiceChip() {
    EchoJournalTheme {
        MultiChoiceChip(
            displayText = "All Topics",
            isClearVisible = true,
            isHighlighted = true,
            isDropDownVisible = true,
            dropDownMenu = {},
            leadingContent = {
                Icon(
                    imageVector = Icons.Default.Build,
                    contentDescription = null,
                )
            },
            onClearButtonClick = {},
            onClick = {},
        )
    }
}
