package com.hantash.echojournal.echo.presentation.echo_list

import com.hantash.echojournal.echo.presentation.echo_list.model.EchoFilterChip
import com.hantash.echojournal.echo.presentation.echo_list.model.TrackSizeInfo
import com.hantash.echojournal.echo.presentation.model.MoodUi

// These are the actions initiated from UI into Viewmodel
sealed interface EchoAction {
    data object OnFabClick: EchoAction
    data object OnFabLongClick: EchoAction
    data object OnSettingsClick: EchoAction
    data class OnRemoveFilters(val filterType: EchoFilterChip): EchoAction
    data object OnMoodClipClick: EchoAction
    data object OnDismissMoodDropDown: EchoAction
    data class OnFilterByMoodClick(val moodUi: MoodUi): EchoAction
    data object OnTopicChipClick: EchoAction
    data object OnDismissTopicDropDown: EchoAction
    data class OnFilterByTopicClick(val topic: String): EchoAction
    data class OnPlayEchoClick(val echoId: Int): EchoAction
    data object OnPauseAudioClick: EchoAction
    data class OnTrackSizeAvailable(val trackSizeInfo: TrackSizeInfo): EchoAction
    data object OnAudioPermissionGranted: EchoAction
    data object OnCancelRecording: EchoAction
    data object OnPauseRecordingClick: EchoAction
    data object OnResumeRecordingClick: EchoAction
    data object OnCompleteRecording: EchoAction
}