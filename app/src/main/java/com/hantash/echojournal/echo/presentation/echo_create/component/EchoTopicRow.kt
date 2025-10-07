package com.hantash.echojournal.echo.presentation.echo_create.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.hantash.echojournal.R
import com.hantash.echojournal.core.presentation.designsystem.chip.HashtagChip
import com.hantash.echojournal.core.presentation.designsystem.menu.Selectable
import com.hantash.echojournal.core.presentation.designsystem.menu.Selectable.Companion.asUnselectedItems
import com.hantash.echojournal.core.presentation.designsystem.menu.SelectableDropDownMenu
import com.hantash.echojournal.core.presentation.designsystem.menu.SelectableOptionExtras
import com.hantash.echojournal.core.presentation.designsystem.text_field.TransparentTextField
import com.hantash.echojournal.core.presentation.designsystem.theme.EchoJournalTheme

@Composable
fun EchoTopicsRow(
    modifier: Modifier = Modifier,
    addTopicText: String,
    showCreateTopicOption: Boolean,
    showTopicSuggestions: Boolean,
    topics: List<String>,
    searchResults: List<Selectable<String>>,
    onTopicClick: (String) -> Unit,
    onDismissTopicSuggestions: () -> Unit,
    onRemoveTopicClick: (String) -> Unit,
    onAddTopicTextChange: (String) -> Unit,
    dropDownMaxHeight: Dp = (LocalConfiguration.current.screenHeightDp * 0.3).dp
) {
    var topicRowHeight by remember {
        mutableIntStateOf(0)
    }
//    var chipHeight by remember {
//        mutableIntStateOf(0)
//    }

    Row(
        modifier = modifier
            .wrapContentHeight()
            .onSizeChanged { topicRowHeight = it.height },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
//            modifier = Modifier
//                .defaultMinSize(
//                    minHeight = with(LocalDensity.current) {
//                        chipHeight.toDp()
//                    }
//                ),
            imageVector = ImageVector.vectorResource(R.drawable.hashtag),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outlineVariant
        )
        FlowRow(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp)
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            topics.forEach { topic ->
                HashtagChip(
//                    modifier = Modifier
//                        .onSizeChanged { chipHeight = it.height },
                    name = topic,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(R.string.remove_topic, topic),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .size(14.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = rememberRipple(),
                                    onClick = {
                                        onRemoveTopicClick(topic)
                                    }
                                )
                        )
                    }
                )
            }
            TransparentTextField(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
//                    .defaultMinSize(
//                        minHeight = with(LocalDensity.current) {
//                            chipHeight.toDp()
//                        }
//                    )
                    .fillMaxHeight(),
                text = addTopicText,
                onValueChange = onAddTopicTextChange,
                maxLines = 1,
                hintText = if(topics.isEmpty()) {
                    stringResource(R.string.topic)
                } else null
            )
        }

        if(showTopicSuggestions) {
            SelectableDropDownMenu(
                items = searchResults,
                itemDisplayText = { it },
                onDismiss = onDismissTopicSuggestions,
                key = { it },
                onItemClick = { onTopicClick(it.item) },
                leadingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.hashtag),
                        contentDescription = null,
                        modifier = Modifier
                            .size(14.dp)
                    )
                },
                maxDropDownHeight = dropDownMaxHeight,
                dropDownOffset = IntOffset(
                    x = 0,
                    y = topicRowHeight
                ),
                dropDownExtras = if(showCreateTopicOption) {
                    SelectableOptionExtras(
                        text = addTopicText,
                        onClick = { onTopicClick(addTopicText) }
                    )
                } else null
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun EchoTopicsRowPreview() {
    EchoJournalTheme {
        EchoTopicsRow(
            topics = listOf("Work", "Love"),
            addTopicText = "he",
            showCreateTopicOption = false,
            showTopicSuggestions = false,
            searchResults = listOf(
                "hello",
                "helloworld"
            ).asUnselectedItems(),
            onTopicClick = {},
            onDismissTopicSuggestions = {},
            onRemoveTopicClick = {},
            onAddTopicTextChange = {},
            modifier = Modifier
                .padding(top = 64.dp)
                .fillMaxSize()
        )
    }
}