package com.anas.eventizer.data.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.anas.eventizer.domain.repo.EventsRepository
import javax.inject.Inject

class UploadPlaceImagesWorker @Inject  constructor(
    private val eventsRepository: EventsRepository,
    context: Context,
    prams:WorkerParameters) : CoroutineWorker(context,prams) {
    override suspend fun doWork(): Result {


        TODO("Not yet implemented")
    }
}