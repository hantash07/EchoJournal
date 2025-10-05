package com.hantash.echojournal.echo.presentation.echo_list.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.hantash.echojournal.R
import com.hantash.echojournal.core.presentation.designsystem.chip.MultiChoiceChip
import com.hantash.echojournal.core.presentation.designsystem.menu.Selectable
import com.hantash.echojournal.core.presentation.designsystem.menu.SelectableDropDownMenu
import com.hantash.echojournal.core.presentation.util.UiText
import com.hantash.echojournal.echo.presentation.echo_list.EchoAction
import com.hantash.echojournal.echo.presentation.echo_list.model.EchoFilterChip
import com.hantash.echojournal.echo.presentation.echo_list.model.MoodChipContent
import com.hantash.echojournal.echo.presentation.model.MoodUi

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EchoFilterRow(
    modifier: Modifier = Modifier,
    moodChipContent: MoodChipContent,
    selectedEchoFilterChip: EchoFilterChip?,
    hasActiveMoodFilters: Boolean,
    moods: List<Selectable<MoodUi>>,
    topicChipTitle: UiText,
    hasActiveTopicFilters: Boolean,
    topics: List<Selectable<String>>,
    onAction: (EchoAction) -> Unit,
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    var dropDownOffset by remember { mutableStateOf(IntOffset.Zero) }
    val dropDownMaxHeight = (configuration.screenHeightDp * 0.3f).dp

    FlowRow( //NOTE: Difference b/w simple Row and FlowRow?
        modifier = modifier
            .padding(16.dp)
            .onGloballyPositioned {
                dropDownOffset = IntOffset(
                    x = 0,
                    y = it.size.height
                )
            },
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        MultiChoiceChip(
            displayText = moodChipContent.title.asString(),
            onClick = {
                onAction(EchoAction.OnMoodClipClick)
            },
            leadingContent = {
                if (moodChipContent.iconsRes.isNotEmpty()) {
                    Row(
                        modifier = Modifier.padding(end = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy((-4).dp)
                    ) {
                        moodChipContent.iconsRes.forEach { iconRes ->
                            Image(
                                modifier = Modifier.height(16.dp),
                                imageVector = ImageVector.vectorResource(iconRes),
                                contentDescription = moodChipContent.title.asString()
                            )
                        }
                    }
                }
            },
            isClearVisible = hasActiveMoodFilters,
            isDropDownVisible = selectedEchoFilterChip == EchoFilterChip.MOODS,
            isHighlighted = hasActiveMoodFilters || selectedEchoFilterChip == EchoFilterChip.MOODS,
            onClearButtonClick = {
                onAction(EchoAction.OnRemoveFilters(EchoFilterChip.MOODS))
            },
            dropDownMenu = {
                SelectableDropDownMenu(
                    items = moods,
                    itemDisplayText = { moodUi -> moodUi.title.asString(context) },
                    onDismiss = {
                        onAction(EchoAction.OnDismissMoodDropDown)
                    },
                    onItemClick = { moodUi ->
                        onAction(EchoAction.OnFilterByMoodClick(moodUi.item))
                    },
                    dropDownOffset = dropDownOffset,
                    maxDropDownHeight = dropDownMaxHeight,
                    leadingIcon = { moodUi ->
                        Image(
                            imageVector = ImageVector.vectorResource(moodUi.iconSet.fill),
                            contentDescription = moodUi.title.asString()
                        )
                    },
                    key = { moodUi -> moodUi.title.asString(context) },
                )
            }
        )

        MultiChoiceChip(
            displayText = topicChipTitle.asString(),
            onClick = {
                onAction(EchoAction.OnTopicChipClick)
            },
            isClearVisible = hasActiveTopicFilters,
            isDropDownVisible = selectedEchoFilterChip == EchoFilterChip.TOPICS,
            isHighlighted = hasActiveTopicFilters || selectedEchoFilterChip == EchoFilterChip.TOPICS,
            onClearButtonClick = {
                onAction(EchoAction.OnRemoveFilters(EchoFilterChip.TOPICS))
            },
            dropDownMenu = {
                if (topics.isEmpty()) {
                    SelectableDropDownMenu(
                        items = listOf(Selectable(
                            item = stringResource(R.string.you_don_t_have_any_topics_yet),
                            selected = false
                        )),
                        itemDisplayText = { it },
                        key = { it },
                        onDismiss = {
                            onAction(EchoAction.OnDismissTopicDropDown)
                        },
                        onItemClick = {}
                    )
                } else {
                    SelectableDropDownMenu(
                        items = topics,
                        itemDisplayText = { topic -> topic },
                        onDismiss = {
                            onAction(EchoAction.OnDismissTopicDropDown)
                        },
                        onItemClick = { topic ->
                            onAction(EchoAction.OnFilterByTopicClick(topic.item))
                        },
                        dropDownOffset = dropDownOffset,
                        maxDropDownHeight = dropDownMaxHeight,
                        leadingIcon = { topic ->
                            Image(
                                imageVector = ImageVector.vectorResource(R.drawable.hashtag),
                                contentDescription = topic
                            )
                        },
                        key = { topic -> topic },
                    )
                }
            }
        )
    }
}