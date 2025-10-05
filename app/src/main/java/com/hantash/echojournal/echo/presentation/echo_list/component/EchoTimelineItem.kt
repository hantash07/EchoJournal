package com.hantash.echojournal.echo.presentation.echo_list.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hantash.echojournal.core.presentation.designsystem.theme.EchoJournalTheme
import com.hantash.echojournal.echo.presentation.echo_list.model.RelativePosition
import com.hantash.echojournal.echo.presentation.echo_list.model.TrackSizeInfo
import com.hantash.echojournal.echo.presentation.model.EchoUi
import com.hantash.echojournal.echo.presentation.preview.PreviewModel

private val noVerticalLineAboveIconModifier = Modifier.padding(top = 16.dp)
private val noVerticalLineBelowIconModifier = Modifier.height(8.dp)

@Composable
fun EchoTimelineItem(
    modifier: Modifier = Modifier,
    echoUi: EchoUi,
    relativePosition: RelativePosition,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    onTrackSizeAvailable: (TrackSizeInfo) -> Unit
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
    ) {

        Box(
            modifier = Modifier.fillMaxHeight(),
            contentAlignment = Alignment.TopCenter
        ) {
            if (relativePosition != RelativePosition.SINGLE_ENTRY) {
                VerticalDivider(
                    modifier = when(relativePosition) {
                        RelativePosition.FIRST -> noVerticalLineAboveIconModifier
                        RelativePosition.LAST -> noVerticalLineBelowIconModifier
                        RelativePosition.IN_BETWEEN -> Modifier
                        else -> Modifier
                    }
                )
            }

            Image(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .size(32.dp),
                imageVector = ImageVector.vectorResource(echoUi.moodUi.iconSet.fill),
                contentDescription = echoUi.title
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        EchoCard(
            modifier = Modifier.padding(vertical = 8.dp),
            echoUi = echoUi,
            onPlayClick = onPlayClick,
            onPauseClick = onPauseClick,
            onTrackSizeAvailable = onTrackSizeAvailable
        )

    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    EchoJournalTheme {
        EchoTimelineItem(
            modifier = Modifier.fillMaxWidth(),
            echoUi = PreviewModel.echoUi,
            relativePosition = RelativePosition.LAST,
            onPlayClick = {},
            onPauseClick = {},
            onTrackSizeAvailable = {}
        )
    }
}