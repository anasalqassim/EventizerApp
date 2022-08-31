package com.anas.eventizer.data.repo

import com.anas.eventizer.data.remote.EventsFirestoreDataSource
import com.anas.eventizer.domain.models.PersonalEvent
import com.anas.eventizer.domain.repo.PersonalEventsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PersonalEventsRepositoryImpl @Inject constructor(
     private val  personalEventsFirestoreDataSource: EventsFirestoreDataSource,
     private val externalScope:CoroutineScope
    )
    : PersonalEventsRepository {

    //for thread safe var caching
    private val latestPersonalEventsMutex = Mutex()

    //for var caching
    private var latestPersonalEvents: List<PersonalEvent> = emptyList()


    override suspend fun getPersonalEvents(userId: String,refresh:Boolean): List<PersonalEvent> {

        return  if (refresh || latestPersonalEvents.isEmpty()) {
            withContext(externalScope.coroutineContext) {
                personalEventsFirestoreDataSource
                    .getPersonalEvents(userId).also { fetchedLatestPersonalEvent ->
                        latestPersonalEventsMutex.withLock {
                            latestPersonalEvents = fetchedLatestPersonalEvent
                        }
                    }
            }

        }else{
            latestPersonalEventsMutex.withLock { this.latestPersonalEvents}
        }

    }




}