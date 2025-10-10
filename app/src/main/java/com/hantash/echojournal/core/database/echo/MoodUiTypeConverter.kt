package com.hantash.echojournal.core.database.echo

import androidx.room.TypeConverter
import com.hantash.echojournal.echo.presentation.model.MoodUi

class MoodUiTypeConverter {
    @TypeConverter
    fun fromMood(moodUi: MoodUi): String {
        return moodUi.name
    }

    @TypeConverter
    fun toMood(moodName: String): MoodUi {
        return MoodUi.valueOf(moodName)
    }
}