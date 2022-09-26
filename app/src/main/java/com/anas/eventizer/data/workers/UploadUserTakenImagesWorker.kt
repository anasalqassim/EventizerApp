package com.anas.eventizer.data.workers

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.anas.eventizer.data.EventsDataStore
import com.anas.eventizer.data.remote.EventsTasksDataSource
import com.anas.eventizer.domain.repo.EventsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

class UploadUserTakenImagesWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted val prams: WorkerParameters,
    private val eventsRepository: EventsRepository
) : CoroutineWorker(context,prams)  {
    override suspend fun doWork(): Result {

        val eventId = EventsDataStore.getEventId(context).first()
        val eventType = EventsDataStore.getEventType(context).first()

        val imageUris = inputData
            .getStringArray(EventsTasksDataSource.IMAGES_URI_KEY)





        if (eventId != "none" && eventType != "none" && imageUris != null){
            eventsRepository.uploadEventUserTakenImages(eventId,eventType,imageUris)
        }

        return Result.success()
    }

    @AssistedFactory
    interface Factory {
        fun create(appContext: Context, params: WorkerParameters): UploadPlaceImagesWorker
    }
}