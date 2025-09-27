package com.hantash.echojournal.echo.di

import com.hantash.echojournal.echo.data.recoding.AndroidVoiceRecorder
import com.hantash.echojournal.echo.domain.recording.VoiceRecorder
import com.hantash.echojournal.echo.presentation.echo.EchoViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val echoModule = module {
    single {
        AndroidVoiceRecorder(
            context = androidApplication(),
            applicationScope = get()
        )
    } bind VoiceRecorder::class

    viewModelOf(::EchoViewModel)
}