package com.hantash.echojournal.app

import android.app.Application
import com.hantash.echojournal.BuildConfig
import com.hantash.echojournal.app.di.appModule
import com.hantash.echojournal.echo.di.echoModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class EchoJournalApp: Application() {
    val applicationScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@EchoJournalApp)
            modules(
                appModule,
                echoModule
            )
        }
    }

}