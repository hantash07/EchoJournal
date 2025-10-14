package com.hantash.echojournal.echo.domain.settings

import com.hantash.echojournal.echo.domain.echo.Mood
import kotlinx.coroutines.flow.Flow

interface SettingsPreferences {
    suspend fun saveDefaultMood(mood: Mood)
    fun observeDefaultMood(): Flow<Mood>

    suspend fun saveDefaultTopics(topics: List<String>)
    fun observeDefaultTopics(): Flow<List<String>>
}