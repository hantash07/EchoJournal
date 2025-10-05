package com.hantash.echojournal.echo.presentation.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.hantash.echojournal.R
import com.hantash.echojournal.core.presentation.designsystem.theme.EchoJournalTheme
import com.hantash.echojournal.core.presentation.designsystem.theme.Pause
import com.hantash.echojournal.core.presentation.util.defaultShadow
import com.hantash.echojournal.echo.presentation.model.MoodUi
import com.hantash.echojournal.echo.presentation.echo_list.model.PlaybackState

@Composable
fun EchoPlaybackButton(
    modifier: Modifier = Modifier,
    playbackState: PlaybackState,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    colors: IconButtonColors,
) {

    FilledIconButton(
        modifier = modifier.defaultShadow(),
        onClick = when(playbackState) {
            PlaybackState.PLAYING -> onPauseClick
            PlaybackState.PAUSED,
            PlaybackState.STOPPED -> onPlayClick
        },
        colors = colors
    ) {
        Icon(
            imageVector = when(playbackState) {
                PlaybackState.PLAYING -> Icons.Filled.Pause
                PlaybackState.PAUSED,
                PlaybackState.STOPPED -> Icons.Filled.PlayArrow
            },
            contentDescription = when(playbackState) {
                PlaybackState.PLAYING -> stringResource(R.string.playing)
                PlaybackState.PAUSED -> stringResource(R.string.paused)
                PlaybackState.STOPPED -> stringResource(R.string.stopped)
            }
        )
    }

}

@Preview
@Composable
private fun Preview() {
    EchoJournalTheme {
        EchoPlaybackButton(
            playbackState = PlaybackState.PLAYING,
            onPauseClick = {},
            onPlayClick = {},
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MoodUi.SAD.colorSet.vivid
            )
        )
    }
}