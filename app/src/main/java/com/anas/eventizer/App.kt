package com.anas.eventizer

import android.app.Application
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application(){
    override fun onCreate() {
        super.onCreate()
        Places.initialize(this,BuildConfig.MAPS_API_KEY)
    }
}