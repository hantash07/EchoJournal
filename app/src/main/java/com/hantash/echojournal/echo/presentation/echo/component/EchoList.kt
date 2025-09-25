@file:OptIn(ExperimentalFoundationApi::class)

package com.hantash.echojournal.echo.presentation.echo.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hantash.echojournal.core.presentation.designsystem.theme.EchoJournalTheme
import com.hantash.echojournal.core.presentation.util.UiText
import com.hantash.echojournal.echo.presentation.echo.model.EchoDaySection
import com.hantash.echojournal.echo.presentation.echo.model.RelativePosition
import com.hantash.echojournal.echo.presentation.echo.model.TrackSizeInfo
import com.hantash.echojournal.echo.presentation.preview.PreviewModel
import java.time.Instant
import java.time.ZonedDateTime

@Composable
fun EchoList(
    modifier: Modifier = Modifier,
    sections: List<EchoDaySection>,
    onPlayClick: (echoId: Int) -> Unit,
    onPauseClick: () -> Unit,
    onTrackSizeAvailable: (TrackSizeInfo) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp)
    ) {
        sections.forEachIndexed { sectionIndex, (dateHeader, echos) ->
            stickyHeader {
                if (sectionIndex > 0) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = dateHeader.asString().uppercase(),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            itemsIndexed(
                items = echos,
                key = {_, echo -> echo.id}
            ) { index, echo ->
                EchoTimelineItem(
                    echoUi = echo,
                    relativePosition = when {
                        index == 0 && echos.size == 1 -> RelativePosition.SINGLE_ENTRY
                        index == 0 -> RelativePosition.FIRST
                        echos.lastIndex == index -> RelativePosition.LAST
                        else -> RelativePosition.IN_BETWEEN
                    },
                    onPlayClick = { onPlayClick(echo.id) },
                    onPauseClick = onPauseClick,
                    onTrackSizeAvailable = onTrackSizeAvailable
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    EchoJournalTheme {
        val todayEchos = remember {
            (1..3).map {
                PreviewModel.echoUi.copy(
                    id = it,
                    recordedAt = Instant.now()
                )
            }
        }
        val yesterdayEchos = remember {
            (4..6).map {
                PreviewModel.echoUi.copy(
                    id = it,
                    recordedAt = ZonedDateTime.now().minusDays(1).toInstant()
                )
            }
        }
        val twoDaysAgoEchos = remember {
            (4..6).map {
                PreviewModel.echoUi.copy(
                    id = it,
                    recordedAt = ZonedDateTime.now().minusDays(2).toInstant()
                )
            }
        }
        val sections = remember {
            listOf(
                EchoDaySection(
                    dateHeader = UiText.Dynamic("Today"),
                    echos = todayEchos
                ),
                EchoDaySection(
                    dateHeader = UiText.Dynamic("Yesterday"),
                    echos = yesterdayEchos
                ),
                EchoDaySection(
                    dateHeader = UiText.Dynamic("2025/09/22"),
                    echos = twoDaysAgoEchos
                )
            )
        }

        EchoList(
            sections = sections,
            onPlayClick = {},
            onPauseClick = {},
            onTrackSizeAvailable = {}
        )
    }
}