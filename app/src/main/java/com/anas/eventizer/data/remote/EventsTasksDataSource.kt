package com.anas.eventizer.data.remote

import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.anas.eventizer.data.workers.UploadPlaceImagesWorker
import com.anas.eventizer.domain.repo.EventsRepository
import javax.inject.Inject

class EventsTasksDataSource @Inject constructor(
    private val workManager: WorkManager
) {
    fun uploadEventImages(){

        val uploadImagesRequest = OneTimeWorkRequestBuilder<UploadPlaceImagesWorker>()
            .build()
        workManager.enqueue(uploadImagesRequest)
    }

}