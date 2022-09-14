package com.anas.eventizer

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.anas.eventizer.data.WorkersFactory
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application(){


    @Inject
    lateinit var workersFactory: WorkersFactory

    override fun onCreate() {
        super.onCreate()
        Places.initialize(this,BuildConfig.MAPS_API_KEY)

        val workManagerConfig = Configuration.Builder()
            .setWorkerFactory(workersFactory)
            .build()

        WorkManager.initialize(this,workManagerConfig)
    }
}