package com.anas.eventizer.data

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.anas.eventizer.data.workers.UploadPlaceImagesWorker
import javax.inject.Inject

class WorkersFactory @Inject constructor(
    private val uploadPlaceImagesWorkerFactory: UploadPlaceImagesWorker.Factory
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
      return  when(workerClassName){
            UploadPlaceImagesWorker::class.java.name ->{
                uploadPlaceImagesWorkerFactory.create(appContext,workerParameters)
            }
            else -> null
        }
    }
}