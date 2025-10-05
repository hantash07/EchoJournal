package com.hantash.echojournal.echo.presentation.component

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hantash.echojournal.core.presentation.designsystem.theme.EchoJournalTheme
import com.hantash.echojournal.core.presentation.designsystem.theme.MoodPrimary25
import com.hantash.echojournal.core.presentation.designsystem.theme.MoodPrimary35
import com.hantash.echojournal.core.presentation.designsystem.theme.MoodPrimary80
import com.hantash.echojournal.core.presentation.util.formatMMSS
import com.hantash.echojournal.echo.presentation.model.MoodUi
import com.hantash.echojournal.echo.presentation.echo_list.model.PlaybackState
import com.hantash.echojournal.echo.presentation.echo_list.model.TrackSizeInfo
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun EchoMoodPlayer(
    modifier: Modifier = Modifier,
    moodUi: MoodUi?,
    playbackState: PlaybackState,
    durationPlayed: Duration,
    totalPlaybackDuration: Duration,
    amplitudeBarWidth: Dp = 5.dp,
    amplitudeBarSpacing: Dp = 4.dp,
    powerRatios: List<Float>,
    playerProgress: () -> Float,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    onTrackSizeAvailable: (TrackSizeInfo) -> Unit
) {
    val iconTint = when(moodUi) {
        null -> MoodPrimary80
        else -> moodUi.colorSet.vivid
    }
    val trackFillColor = when(moodUi) {
        null -> MoodPrimary80
        else -> moodUi.colorSet.vivid
    }
    val bgColor = when(moodUi) {
        null -> MoodPrimary25
        else -> moodUi.colorSet.faded
    }
    val trackColor = when(moodUi) {
        null -> MoodPrimary35
        else -> moodUi.colorSet.desaturated
    }
    val formattedDurationText = remember(durationPlayed, totalPlaybackDuration) {
        "${durationPlayed.formatMMSS()}/${totalPlaybackDuration.formatMMSS()}"
    }


    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = bgColor
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {

            EchoPlaybackButton(
                playbackState = playbackState,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = iconTint
                ),
                onPlayClick = onPlayClick,
                onPauseClick = onPauseClick
            )

            EchoPlayBar(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 10.dp, horizontal = 8.dp)
                    .fillMaxHeight(),
                amplitudeBarWidth = amplitudeBarWidth,
                amplitudeBarSpacing = amplitudeBarSpacing,
                powerRatios = powerRatios,
                trackColor = trackColor,
                trackFillColor = trackFillColor,
                playerProgress = playerProgress
            )

            Text(
                modifier = Modifier.padding(end = 8.dp),
                text = formattedDurationText,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

        }
    }

}

@Preview
@Composable
private fun Preview() {
    EchoJournalTheme {
        val ratios = remember {
            (1..<30).map {
                Random.nextFloat()
            }
        }
        EchoMoodPlayer(
            modifier = Modifier.fillMaxWidth(),
            moodUi = MoodUi.NEUTRAL,
            playbackState = PlaybackState.PLAYING,
            durationPlayed = 125.seconds,
            totalPlaybackDuration = 250.seconds,
            powerRatios = ratios,
            playerProgress = { 0.3f },
            onPauseClick = {},
            onPlayClick = {},
            onTrackSizeAvailable = {}
        )
    }
}