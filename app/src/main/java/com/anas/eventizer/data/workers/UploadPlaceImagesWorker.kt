package com.anas.eventizer.data.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.anas.eventizer.data.EventsDataStore
import com.anas.eventizer.domain.repo.EventsRepository
import javax.inject.Inject

class UploadPlaceImagesWorker @Inject  constructor(
    private val eventsRepository: EventsRepository,
    private val context: Context,
    prams:WorkerParameters) : CoroutineWorker(context,prams) {
    override suspend fun doWork(): Result {

        val eventId = EventsDataStore.getEventId(context)

        val eventType = EventsDataStore.getEventType(context)




        TODO("Not yet implemented")
    }
}