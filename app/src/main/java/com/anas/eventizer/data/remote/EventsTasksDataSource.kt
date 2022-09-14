package com.anas.eventizer.data.remote

import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.anas.eventizer.data.workers.UploadPlaceImagesWorker
import javax.inject.Inject

class EventsTasksDataSource @Inject constructor(
    private val workManager: WorkManager
) {
    fun uploadEventImages(){

        val uploadImagesRequest = OneTimeWorkRequestBuilder<UploadPlaceImagesWorker>()
            .build()
        workManager.enqueueUniqueWork(
            "work",
            ExistingWorkPolicy.KEEP,
            uploadImagesRequest
        )
    }

}