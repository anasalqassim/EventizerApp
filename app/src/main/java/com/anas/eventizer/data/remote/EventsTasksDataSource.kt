package com.anas.eventizer.data.remote

import android.net.Uri
import androidx.core.net.toUri
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.anas.eventizer.data.workers.UploadPlaceImagesWorker
import com.anas.eventizer.data.workers.UploadUserTakenImagesWorker
import javax.inject.Inject


class EventsTasksDataSource @Inject constructor(
    private val workManager: WorkManager
) {

    companion object{
        const val IMAGES_URI_KEY = "imageUris"
        const val EVENT_TYPE_KEY = "eventType"
        const val EVENT_ID_KEY = "eventId"
    }

    fun uploadEventUserImages(imageURIs:List<Uri>){

        val imagesData = Data.Builder().putStringArray(IMAGES_URI_KEY,
            imageURIs.map { it.toString()}.toTypedArray())
            .build()




        val uploadTask = OneTimeWorkRequestBuilder<UploadUserTakenImagesWorker>()
            .setInputData(imagesData)
            .build()

        workManager.enqueueUniqueWork(
            "uploadEventUserImagesWork",
            ExistingWorkPolicy.KEEP,
            uploadTask
        )

    }

    fun uploadEventPlaceImages(){

        val uploadImagesRequest = OneTimeWorkRequestBuilder<UploadPlaceImagesWorker>()
            .build()

        workManager.enqueueUniqueWork(
            "work",
            ExistingWorkPolicy.KEEP,
            uploadImagesRequest
        )
    }

}