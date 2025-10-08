@file:OptIn(ExperimentalMaterial3Api::class)

package com.hantash.echojournal.echo.presentation.echo_create

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hantash.echojournal.R
import com.hantash.echojournal.core.presentation.designsystem.button.PrimaryButton
import com.hantash.echojournal.core.presentation.designsystem.button.SecondaryButton
import com.hantash.echojournal.core.presentation.designsystem.text_field.TransparentTextField
import com.hantash.echojournal.core.presentation.designsystem.theme.EchoJournalTheme
import com.hantash.echojournal.core.presentation.designsystem.theme.secondary70
import com.hantash.echojournal.core.presentation.designsystem.theme.secondary95
import com.hantash.echojournal.echo.presentation.component.EchoMoodPlayer
import com.hantash.echojournal.echo.presentation.echo_create.component.EchoTopicsRow
import com.hantash.echojournal.echo.presentation.echo_create.component.SelectMoodSheet
import com.hantash.echojournal.echo.presentation.model.MoodUi
import org.koin.androidx.compose.koinViewModel

@Composable
fun EchoCreateRoot(
    onConfirmLeave: () -> Unit,
    viewModel: EchoCreateViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    EchoCreateScreen(
        state = state,
        onAction = viewModel::onAction,
        onConfirmLeave = onConfirmLeave
    )
}

@Composable
fun EchoCreateScreen(
    state: EchoCreateState,
    onAction: (EchoCreateAction) -> Unit,
    onConfirmLeave: () -> Unit,
) {
    BackHandler(
        enabled = !state.showConfirmLeaveDialog
    ) {
        onAction(EchoCreateAction.OnGoBack)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.new_entry),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onAction(EchoCreateAction.OnNavigateBackClick)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = stringResource(R.string.navigate_back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        val descriptionFocusRequester = remember {
            FocusRequester()
        }
        val focusManager = LocalFocusManager.current
        
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if(state.mood == null) {
                    FilledIconButton(
                        onClick = {
                            onAction(EchoCreateAction.OnSelectMoodClick)
                        },
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.secondary95,
                            contentColor = MaterialTheme.colorScheme.secondary70
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = stringResource(R.string.add_mood)
                        )
                    }
                } else {
                    Image(
                        modifier = Modifier
                            .height(32.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(),
                                onClick = {
                                    onAction(EchoCreateAction.OnSelectMoodClick)
                                }
                            ),
                        imageVector = ImageVector.vectorResource(state.mood.iconSet.fill),
                        contentDescription = state.mood.title.asString(),
                        contentScale = ContentScale.FillHeight
                    )
                }

                TransparentTextField(
                    modifier = Modifier
                        .weight(1f),
                    text = state.titleText,
                    onValueChange = {
                        onAction(EchoCreateAction.OnTitleTextChange(it))
                    },
                    hintText = stringResource(R.string.add_title),
                    textStyle = MaterialTheme.typography.headlineLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            descriptionFocusRequester.requestFocus()
                        }
                    )
                )
            }

            EchoMoodPlayer(
                moodUi = state.mood,
                playbackState = state.playbackState,
                playerProgress = { state.durationPlayedRatio },
                durationPlayed = state.durationPlayed,
                totalPlaybackDuration = state.playbackTotalDuration,
                powerRatios = state.playbackAmplitudes,
                onPlayClick = {
                    onAction(EchoCreateAction.OnPlayAudioClick)
                },
                onPauseClick = {
                    onAction(EchoCreateAction.OnPauseAudioClick)
                },
                onTrackSizeAvailable = {
                    onAction(EchoCreateAction.OnTrackSizeAvailable(it))
                }
            )

            EchoTopicsRow(
                topics = state.topics,
                addTopicText = state.addTopicText,
                showCreateTopicOption = state.showCreateTopicOption,
                showTopicSuggestions = state.showTopicSuggestions,
                searchResults = state.searchResults,
                onAddTopicTextChange = {
                    onAction(EchoCreateAction.OnAddTopicTextChange(it))
                },
                onRemoveTopicClick = {
                    onAction(EchoCreateAction.OnRemoveTopicClick(it))
                },
                onTopicClick = {
                    onAction(EchoCreateAction.OnTopicClick(it))
                },
                onDismissTopicSuggestions = {
                    onAction(EchoCreateAction.OnDismissTopicSuggestions)
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = Icons.Filled.Create,
                    contentDescription = stringResource(R.string.add_description),
                    tint = MaterialTheme.colorScheme.outlineVariant
                )
                TransparentTextField(
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(descriptionFocusRequester),
                    text = state.noteText,
                    onValueChange = {
                        onAction(EchoCreateAction.OnNoteTextChange(it))
                    },
                    hintText = stringResource(R.string.add_description),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    )
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SecondaryButton(
                    modifier = Modifier
                        .fillMaxHeight(),
                    name = stringResource(R.string.cancel),
                    onClick = {
                        onAction(EchoCreateAction.OnCancelClick)
                    }
                )
                PrimaryButton(
                    modifier = Modifier.weight(1f),
                    name = stringResource(R.string.save),
                    onClick = {
                        onAction(EchoCreateAction.OnSaveClick)
                    },
                    enabled = state.canSaveEcho,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = stringResource(R.string.save),
                            modifier = Modifier
                                .size(16.dp)
                        )
                    }
                )
            }
        }

        if (state.showMoodSelector) {
            SelectMoodSheet(
                selectedMood = state.selectedMood,
                onMoodClick = { mood ->
                    onAction(EchoCreateAction.OnMoodClick(mood))
                },
                onConfirmClick = {
                    onAction(EchoCreateAction.OnConfirmMood)
                },
                onDismiss = {
                    onAction(EchoCreateAction.OnDismissMoodSelector)
                }
            )
        }

        if(state.showConfirmLeaveDialog) {
            AlertDialog(
                onDismissRequest = {
                    onAction(EchoCreateAction.OnDismissConfirmLeaveDialog)
                },
                confirmButton = {
                    TextButton(
                        onClick = onConfirmLeave,
                    ) {
                        Text(
                            text = stringResource(R.string.discard),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            onAction(EchoCreateAction.OnDismissConfirmLeaveDialog)
                        },
                    ) {
                        Text(text = stringResource(R.string.cancel))
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.discard_recording)
                    )
                },
                text = {
                    Text(
                        text = stringResource(R.string.this_cannot_be_undone)
                    )
                }
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    EchoJournalTheme {
        EchoCreateScreen(
            state = EchoCreateState(
                mood = MoodUi.EXCITED,
                canSaveEcho = true
            ),
            onAction = {},
            onConfirmLeave = {}
        )
    }
}