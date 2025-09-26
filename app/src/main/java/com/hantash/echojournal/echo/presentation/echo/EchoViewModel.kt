package com.hantash.echojournal.echo.presentation.echo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hantash.echojournal.R
import com.hantash.echojournal.core.presentation.designsystem.menu.Selectable
import com.hantash.echojournal.core.presentation.util.UiText
import com.hantash.echojournal.echo.presentation.echo.model.EchoFilterChip
import com.hantash.echojournal.echo.presentation.echo.model.MoodChipContent
import com.hantash.echojournal.echo.presentation.model.MoodUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class EchoViewModel: ViewModel() {
    private var hasLoadedInitialData = false

    //NOTE: Difference b/w State and Event in detail.
    private val _state = MutableStateFlow(EchoState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeFilters() //NOTE: Not a better approach to load initial data. RND on recommended approach.
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = EchoState()
        )

    private val selectedMoodFilters = MutableStateFlow<List<MoodUi>>(emptyList())
    private val selectedTopicFilters = MutableStateFlow<List<String>>(emptyList())

    fun onAction(action: EchoAction) {
        when (action) {
            EchoAction.OnFabClick -> {}
            EchoAction.OnFabLongClick -> {}
            EchoAction.OnSettingsClick -> {}

            EchoAction.OnMoodClipClick -> {
                _state.update { it.copy(
                    selectedEchoFilterChip = EchoFilterChip.MOODS
                ) }
            }
            EchoAction.OnDismissMoodDropDown -> {
                _state.update { it.copy(
                    selectedEchoFilterChip = null
                ) }
            }
            is EchoAction.OnFilterByMoodClick -> {
                toggleMoodFilter(action.moodUi)
            }

            EchoAction.OnTopicChipClick -> {
                _state.update { it.copy(
                    selectedEchoFilterChip = EchoFilterChip.TOPICS
                ) }
            }
            EchoAction.OnDismissTopicDropDown -> {
                _state.update { it.copy(
                    selectedEchoFilterChip = null
                ) }
            }
            is EchoAction.OnFilterByTopicClick -> {
                toggleTopicFilter(action.topic)
            }

            is EchoAction.OnRemoveFilters -> {
                when(action.filterType) {
                    EchoFilterChip.MOODS -> selectedMoodFilters.update { emptyList() }
                    EchoFilterChip.TOPICS -> selectedTopicFilters.update { emptyList() }
                }
            }

            EchoAction.OnPauseClick -> {}
            is EchoAction.OnPlayEchoClick -> {}
            is EchoAction.OnTrackSizeAvailable -> {}
        }
    }

    private fun toggleMoodFilter(moodUi: MoodUi) {
        selectedMoodFilters.update { selectedMoods ->
            if (moodUi in selectedMoods) {
                selectedMoods - moodUi
            } else {
                selectedMoods + moodUi
            }
        }
    }

    private fun toggleTopicFilter(topic: String) {
        selectedTopicFilters.update { selectedTopics ->
            if (topic in selectedTopics) {
                selectedTopics - topic
            } else {
                selectedTopics + topic
            }
        }
    }

    private fun observeFilters() {
        combine(
            selectedTopicFilters,
            selectedMoodFilters
        ) { selectedTopics, selectedMoods ->
            _state.update { it.copy(
                topicChipTitle = selectedTopics.deriveTopicsToText(),
                hasActiveTopicFilters = selectedTopics.isNotEmpty(),
                topics = it.topics.map { selectableTopic ->
                    Selectable(
                        item = selectableTopic.item,
                        selected = selectedTopics.contains(selectableTopic.item)
                    )
                },
                moodChipContent = selectedMoods.asMoodChipContent(),
                hasActiveMoodFilters = selectedMoods.isNotEmpty(),
                moods = MoodUi.entries.map { mood ->
                    Selectable(
                        item = mood,
                        selected = selectedMoods.contains(mood)
                    )
                },
            ) }
        }.launchIn(viewModelScope)
    }

    private fun List<String>.deriveTopicsToText(): UiText {
        return when(size) {
            0 -> UiText.StringResource(R.string.all_topics)
            1 -> UiText.Dynamic(this.first())
            2 -> UiText.Dynamic("${this.first()}, ${this.last()}")
            else -> {
                val extraElementCount = size - 2
                UiText.Dynamic("${this.first()}, ${this[1]} +$extraElementCount")
            }
        }
    }

    private fun List<MoodUi>.asMoodChipContent(): MoodChipContent {
        if (this.isEmpty()) {
            return MoodChipContent()
        }

        val icons = this.map { it.iconSet.fill }
        val names = this.map { it.title }

        return when(size) {
            1 -> MoodChipContent(
                iconsRes = icons,
                title = names.first()
            )
            2 -> MoodChipContent(
                iconsRes = icons,
                title = UiText.Combined(
                    format = "%s, %s",
                    uiTexts = names.toTypedArray()
                )
            )
            else -> {
                val extraElementCount = size - 2
                MoodChipContent(
                    iconsRes = icons,
                    title = UiText.Combined(
                        format = "%s, %s +$extraElementCount",
                        uiTexts = names.toTypedArray()
                    )
                )
            }
        }
    }

}