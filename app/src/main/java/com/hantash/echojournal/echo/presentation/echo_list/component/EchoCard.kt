package com.hantash.echojournal.echo.presentation.echo_list.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hantash.echojournal.core.presentation.designsystem.chip.HashtagChip
import com.hantash.echojournal.core.presentation.designsystem.theme.EchoJournalTheme
import com.hantash.echojournal.core.presentation.util.defaultShadow
import com.hantash.echojournal.echo.presentation.component.EchoMoodPlayer
import com.hantash.echojournal.echo.presentation.echo_list.model.TrackSizeInfo
import com.hantash.echojournal.echo.presentation.model.EchoUi
import com.hantash.echojournal.echo.presentation.preview.PreviewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EchoCard(
    modifier: Modifier = Modifier,
    echoUi: EchoUi,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    onTrackSizeAvailable: (TrackSizeInfo) -> Unit
) {
    Surface(
        modifier = modifier
            .defaultShadow(shape = RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = modifier.weight(1f),
                    text = echoUi.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = echoUi.formattedRecordedAt,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            EchoMoodPlayer(
                moodUi = echoUi.moodUi,
                playbackState = echoUi.playbackState,
                playerProgress = { echoUi.playbackRatio },
                durationPlayed = echoUi.playbackCurrentDuration,
                totalPlaybackDuration = echoUi.playbackTotalDuration,
                powerRatios = echoUi.amplitudes,
                onPlayClick = onPlayClick,
                onPauseClick = onPauseClick,
                onTrackSizeAvailable = onTrackSizeAvailable
            )

            if (!echoUi.note.isNullOrBlank()) {
                EchoExpandableText(text = echoUi.note)
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
               echoUi.topics.forEach { topic ->
                   HashtagChip(name = topic)
               }
            }

        }
    }
}

@Preview
@Composable
private fun Preview() {
    EchoJournalTheme {
        EchoCard(
            modifier = Modifier,
            echoUi = PreviewModel.echoUi,
            onPlayClick = {},
            onPauseClick = {},
            onTrackSizeAvailable = {}
        )
    }
}