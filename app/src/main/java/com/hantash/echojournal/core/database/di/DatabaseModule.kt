package com.hantash.echojournal.core.database.di

import androidx.room.Room
import com.hantash.echojournal.core.database.EchoDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single<EchoDatabase> {
        Room.databaseBuilder<EchoDatabase>(
            androidApplication(),
            "echo_journal.db",
        ).build()
    }

    single {
        get<EchoDatabase>().echoDao
    }
}