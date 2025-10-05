package com.hantash.echojournal.echo.presentation.echo_list

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
import com.hantash.echojournal.echo.presentation.echo_list.component.EchoEmptyBackground
import com.hantash.echojournal.echo.presentation.echo_list.component.EchoFilterRow
import com.hantash.echojournal.echo.presentation.echo_list.component.EchoList
import com.hantash.echojournal.echo.presentation.echo_list.component.EchoQuickRecordFloatingActionButton
import com.hantash.echojournal.echo.presentation.echo_list.component.EchoRecordingSheet
import com.hantash.echojournal.echo.presentation.echo_list.component.EchoTopBar
import com.hantash.echojournal.echo.presentation.echo_list.model.RecordingState
import org.koin.androidx.compose.koinViewModel
import com.hantash.echojournal.R
import com.hantash.echojournal.core.presentation.util.isAppInForeground
import com.hantash.echojournal.echo.domain.recording.RecordingDetail
import timber.log.Timber

@Composable
fun EchoRoot(
    onNavToCreateEcho: (RecordingDetail) -> Unit,
    viewModel: EchoViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // NOTE: Handle the other cases. Example user decline the permissions
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Timber.d("Permissions is Granted!")
            viewModel.onAction(EchoAction.OnAudioPermissionGranted)
        }
    }

    val context = LocalContext.current
    ObserveAsEvents(viewModel.events) { event ->
        when(event) {
            is EchoEvent.RequestAudioPermission -> {
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                Timber.d("Requested for Permissions")
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
                onNavToCreateEcho(event.recordingDetail)
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
            EchoQuickRecordFloatingActionButton(
                isQuickRecording = state.recordingState == RecordingState.QUICK_CAPTURE,
                onClick = {
                    onAction(EchoAction.OnFabClick)
                },
                onLongPressStart = {
                    //NOTE: There is a glitch when permission is not granted.
                    onAction(EchoAction.OnFabLongClick)
                },
                onLongPressEnd = { isCancelled ->
                    if (isCancelled) {
                        onAction(EchoAction.OnCancelRecording)
                    } else {
                        onAction(EchoAction.OnCompleteRecording)
                    }
                }
            )
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
                onCompleteRecording = { onAction(EchoAction.OnCompleteRecording) }
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
