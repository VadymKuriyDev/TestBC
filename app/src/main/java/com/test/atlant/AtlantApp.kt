package com.test.atlant

import android.app.Application
import com.test.atlant.di.navigationLoginModule
import com.test.atlant.di.navigationModule
import com.test.atlant.di.retrofitModule
import com.test.atlant.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class AtlantApp : Application() {

    var listOfModules = listOf(
        retrofitModule,
        navigationLoginModule,
        navigationModule,
        viewModelModule)

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AtlantApp)
            modules(listOfModules)
        }
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}