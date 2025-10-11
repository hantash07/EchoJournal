package com.hantash.echojournal.echo.di

import com.hantash.echojournal.echo.data.echo.RoomEchoDataSource
import com.hantash.echojournal.echo.data.recoding.AndroidVoiceRecorder
import com.hantash.echojournal.echo.data.recoding.InternalRecordingStorage
import com.hantash.echojournal.echo.domain.echo.EchoDataSource
import com.hantash.echojournal.echo.domain.recording.RecordingStorage
import com.hantash.echojournal.echo.domain.recording.VoiceRecorder
import com.hantash.echojournal.echo.presentation.echo_create.EchoCreateViewModel
import com.hantash.echojournal.echo.presentation.echo_list.EchoViewModel
import com.plcoding.echojournal.echos.data.audio.AndroidAudioPlayer
import com.plcoding.echojournal.echos.domain.audio.AudioPlayer
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val echoModule = module {
//    single {
//        AndroidVoiceRecorder(
//            context = androidApplication(),
//            applicationScope = get()
//        )
//    } bind VoiceRecorder::class
    singleOf(::AndroidVoiceRecorder) bind VoiceRecorder::class
    singleOf(::InternalRecordingStorage) bind RecordingStorage::class
    singleOf(::AndroidAudioPlayer) bind AudioPlayer::class
    singleOf(::RoomEchoDataSource) bind EchoDataSource::class

    viewModelOf(::EchoViewModel)
    viewModelOf(::EchoCreateViewModel)
}