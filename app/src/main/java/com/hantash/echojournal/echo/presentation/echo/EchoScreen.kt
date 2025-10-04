package com.hantash.echojournal.echo.presentation.echo

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hantash.echojournal.core.presentation.designsystem.theme.EchoJournalTheme
import com.hantash.echojournal.core.presentation.designsystem.theme.bgGradient
import com.hantash.echojournal.core.presentation.util.ObserveAsEvents
import com.hantash.echojournal.echo.presentation.echo.component.EchoEmptyBackground
import com.hantash.echojournal.echo.presentation.echo.component.EchoFilterRow
import com.hantash.echojournal.echo.presentation.echo.component.EchoList
import com.hantash.echojournal.echo.presentation.echo.component.EchoRecordFloatingActionButton
import com.hantash.echojournal.echo.presentation.echo.component.EchoRecordingSheet
import com.hantash.echojournal.echo.presentation.echo.component.EchoTopBar
import com.hantash.echojournal.echo.presentation.echo.model.AudioCaptureMethod
import com.hantash.echojournal.echo.presentation.echo.model.RecordingState
import org.koin.androidx.compose.koinViewModel
import com.hantash.echojournal.R
import com.hantash.echojournal.core.presentation.util.isAppInForeground
import com.hantash.echojournal.echo.presentation.echo.component.SheetContent
import timber.log.Timber

@Composable
fun EchoRoot(
    viewModel: EchoViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // NOTE: Handle the other cases. Example user decline the permissions
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted && state.currentAudioCaptureMethod == AudioCaptureMethod.STANDARD) {
            viewModel.onAction(EchoAction.OnAudioPermissionGranted)
        }
    }

    val context = LocalContext.current
    ObserveAsEvents(viewModel.events) { event ->
        when(event) {
            is EchoEvent.RequestAudioPermission -> {
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
            is EchoEvent.RecordingTooShort -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.audio_recording_was_too_short),
                    Toast.LENGTH_LONG
                ).show()
            }
            is EchoEvent.OnDoneRecording -> {
                Timber.d("Recording Successful!")
            }
        }
    }

    val isAppInForeground by isAppInForeground()
    LaunchedEffect(isAppInForeground, state.recordingState) {
        if (!isAppInForeground && state.recordingState == RecordingState.NORMAL_CAPTURE) {
            viewModel.onAction(EchoAction.OnPauseRecordingClick)
        }
    }

    EchoScreen(state = state, onAction = viewModel::onAction) //NOTE: What does this mean viewModel::onAction
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
            EchoRecordFloatingActionButton(onClick = {
                onAction(EchoAction.OnFabClick)
            })
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
                hasActiveMoodFilters = state.hasActiveMoodFilters,
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

                else -> {
                    EchoList(
                        sections = state.echoDaySelections,
                        onPlayClick = {
                            onAction(EchoAction.OnPlayEchoClick(it))
                        },
                        onPauseClick = {
                            onAction(EchoAction.OnPauseAudioClick)
                        },
                        onTrackSizeAvailable = { trackSize ->
                            onAction(EchoAction.OnTrackSizeAvailable(trackSize))
                        }
                    )
                }
            }
        }

        // Showing bottom sheet recording
        if (state.recordingState in listOf(RecordingState.NORMAL_CAPTURE, RecordingState.PAUSED)) {
            EchoRecordingSheet(
                formattedRecordDuration = state.formattedRecordDuration,
                isRecording = state.recordingState == RecordingState.NORMAL_CAPTURE,
                onDismiss = { onAction(EchoAction.OnCancelRecording) },
                onPauseClick = { onAction(EchoAction.OnPauseRecordingClick) },
                onResumeClick = { onAction(EchoAction.OnResumeRecordingClick) },
                onCompleteRecording = { onAction(EchoAction.OnCompleteRecordingClick) }
            )
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
