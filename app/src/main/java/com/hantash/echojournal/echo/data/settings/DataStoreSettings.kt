package com.hantash.echojournal.echo.data.settings

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.hantash.echojournal.echo.domain.echo.Mood
import com.hantash.echojournal.echo.domain.settings.SettingsPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class DataStoreSettings(
    private val context: Context
): SettingsPreferences {
    companion object {
        private val Context.settingsDataStore by preferencesDataStore(
            name = "settings_datastore"
        )
    }

    private val moodKey = stringPreferencesKey("mood")
    private val topicsKey = stringSetPreferencesKey("topics")

    override suspend fun saveDefaultMood(mood: Mood) {
        context.settingsDataStore.edit { prefs ->
            prefs[moodKey] = mood.name
        }
    }

    override fun observeDefaultMood(): Flow<Mood> {
       return context.settingsDataStore
           .data
           .map { pref ->
               pref[moodKey]?.let {
                   Mood.valueOf(it)
               } ?: Mood.NEUTRAL
           }
           .distinctUntilChanged() //NOTE: What does this do?
    }

    override suspend fun saveDefaultTopics(topics: List<String>) {
        context.settingsDataStore.edit { pref ->
            pref[topicsKey] = topics.toSet()
        }
    }

    override fun observeDefaultTopics(): Flow<List<String>> {
        return context.settingsDataStore
            .data
            .map { pref ->
                pref[topicsKey]?.toList() ?: emptyList()
            }
            .distinctUntilChanged()
    }
}