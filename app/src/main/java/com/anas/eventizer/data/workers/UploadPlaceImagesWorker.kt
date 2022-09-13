package com.anas.eventizer.data.workers

import android.app.Application
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.anas.eventizer.data.EventsDataStore
import com.anas.eventizer.data.repo.EventsRepositoryImpl
import com.anas.eventizer.domain.repo.EventsRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.single
import javax.inject.Inject

class UploadPlaceImagesWorker @Inject  constructor(
    private val context: Context,
     prams:WorkerParameters) : CoroutineWorker(context,prams) {


    override suspend fun doWork(): Result {



//         EventsDataStore.getEventId(context)
//            .collect{ eventId ->
//                 EventsDataStore.getEventType(context)
//                    .collect{ eventType ->
//
//                        eventsRepository.uploadEventImages(eventId,eventType)
//                    }
//        }




        return Result.success()

    }
}