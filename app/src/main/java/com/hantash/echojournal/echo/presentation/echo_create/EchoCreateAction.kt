package com.hantash.echojournal.echo.presentation.echo_create

import com.hantash.echojournal.echo.presentation.echo_list.model.TrackSizeInfo
import com.hantash.echojournal.echo.presentation.model.MoodUi

sealed interface EchoCreateAction {
    data object OnNavigateBackClick: EchoCreateAction
    data class OnTitleTextChange(val text: String): EchoCreateAction
    data class OnAddTopicTextChange(val text: String): EchoCreateAction
    data class OnNoteTextChange(val text: String): EchoCreateAction
    data object OnSelectMoodClick: EchoCreateAction
    data object OnDismissMoodSelector: EchoCreateAction
    data class OnMoodClick(val moodUi: MoodUi): EchoCreateAction
    data object OnConfirmMood: EchoCreateAction
    data class OnTopicClick(val topic: String): EchoCreateAction
    data object OnDismissTopicSuggestions: EchoCreateAction
    data object OnCancelClick: EchoCreateAction
    data object OnSaveClick: EchoCreateAction
    data object OnPlayAudioClick: EchoCreateAction
    data object OnPauseAudioClick: EchoCreateAction
    data class OnTrackSizeAvailable(val trackSizeInfo: TrackSizeInfo): EchoCreateAction
    data class OnRemoveTopicClick(val topic: String): EchoCreateAction
}