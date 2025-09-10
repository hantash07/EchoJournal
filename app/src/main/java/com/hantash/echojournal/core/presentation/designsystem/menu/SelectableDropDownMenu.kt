package com.hantash.echojournal.core.presentation.designsystem.menu

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.hantash.echojournal.R
import com.hantash.echojournal.core.presentation.designsystem.menu.Selectable.Companion.asUnselectedItems
import com.hantash.echojournal.core.presentation.designsystem.theme.EchoJournalTheme

@Composable
fun <T> SelectableDropDownMenu( //NOTE: Explore this generic function in detail.
    modifier: Modifier = Modifier,
    key: (T) -> Any,
    items: List<Selectable<T>>,
    itemDisplayText: (T) -> String, //This is a lambda function because the type is generic. This is not needed if we only have one type of data. Same scenarios for key and leadingIcon.
    dropDownOffset: IntOffset = IntOffset.Zero,
    dropDownExtras: SelectableOptionExtras? = null,
    maxDropDownHeight: Dp = Dp.Unspecified,
    leadingIcon: (@Composable (T) -> Unit)? = null,
    onDismiss: () -> Unit,
    onItemClick: (Selectable<T>) -> Unit
) {
    Popup(
        onDismissRequest = onDismiss,
        offset = dropDownOffset,
    ) {
        Surface(
            modifier = modifier
                .heightIn(max = maxDropDownHeight)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(10.dp),
            shadowElevation = 4.dp
        ) {
            LazyColumn(
                modifier = Modifier
                    .animateContentSize()
                    .padding(6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(
                    items = items,
                    key = { key(it.item) }
                ) { selectable ->
                    Row(
                        modifier = Modifier
                            .animateItem()
                            .fillMaxWidth()
                            .background(
                                color = if (selectable.selected) {
                                    MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.05f)
                                } else {
                                    MaterialTheme.colorScheme.surface
                                }
                            )
                            .clickable {
                                onItemClick(selectable)
                            }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        leadingIcon?.invoke(selectable.item)

                        Text(
                            modifier = Modifier.weight(1f),
                            text = itemDisplayText(selectable.item)
                        )

                        if (selectable.selected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
                if (dropDownExtras != null && dropDownExtras.text.isNotEmpty()) {
                    item(key = true) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .clickable {
                                    dropDownExtras.onClick
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(18.dp),
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                            )

                            Text(
                                modifier = Modifier.weight(1f),
                                text = stringResource(R.string.create_entry, dropDownExtras.text),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewSelectableDropDownMenu() {
    EchoJournalTheme {
        SelectableDropDownMenu(
            items = listOf(
                Selectable(
                    item = "Topic's Item 1",
                    selected = true
                ),
                Selectable(
                    item = "Topic's Item 2",
                    selected = false
                ),
                Selectable(
                    item = "Topic's Item 3",
                    selected = false
                ),
                Selectable(
                    item = "Topic's Item 4",
                    selected = true
                ),
                Selectable(
                    item = "Topic's Item 5",
                    selected = true
                )
            ),
            key = { it },
            itemDisplayText = { it },
            maxDropDownHeight = 500.dp,
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.hashtag),
                    contentDescription = null
                )
            },
            dropDownExtras = SelectableOptionExtras(
                text = stringResource(R.string.add_topic),
                onClick = {}
            ),
            onDismiss = {},
            onItemClick = {}
        )
    }
}
