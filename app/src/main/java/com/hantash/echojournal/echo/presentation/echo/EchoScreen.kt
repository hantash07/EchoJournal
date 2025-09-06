package com.hantash.echojournal.echo.presentation.echo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hantash.echojournal.core.presentation.designsystem.theme.EchoJournalTheme
import com.hantash.echojournal.core.presentation.designsystem.theme.bgGradient
import com.hantash.echojournal.echo.presentation.echo.component.EchoEmptyBackground
import com.hantash.echojournal.echo.presentation.echo.component.EchoFilterRow
import com.hantash.echojournal.echo.presentation.echo.component.EchoRecordFloatingActionButton
import com.hantash.echojournal.echo.presentation.echo.component.EchoTopBar

@Composable
fun EchoRoot(
    viewModel: EchoViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    EchoScreen(state = state, onAction = viewModel::onAction)
}

@Composable
fun EchoScreen(
    state: EchoState,
    onAction: (EchoAction) -> Unit
) {
    Scaffold(
        topBar = {
            EchoTopBar(
                onSettingsClick = {
                    onAction(EchoAction.OnSettingsClick)
                }
            )
        },
        floatingActionButton = {
            EchoRecordFloatingActionButton(onClick = {})
        },

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = MaterialTheme.colorScheme.bgGradient
                )
                .padding(paddingValues)
        ) {
            EchoFilterRow(
                modifier = Modifier.fillMaxWidth(),
                moods = state.moods,
                moodChipContent = state.moodChipContent,
                hasActiveModeFilters = state.hasActiveModeFilters,
                topics = state.topics,
                topicChipTitle = state.topicChipTitle,
                hasActiveTopicFilters = state.hasActiveTopicFilters,
                selectedEchoFilterChip = state.selectedEchoFilterChip,
                onAction = onAction
            )

            when {
                state.isLoadingData -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                !state.hasEchoRecorded -> {
                    EchoEmptyBackground()
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    EchoJournalTheme {
        EchoScreen(
            state = EchoState(),
            onAction = {}
        )
    }
}
