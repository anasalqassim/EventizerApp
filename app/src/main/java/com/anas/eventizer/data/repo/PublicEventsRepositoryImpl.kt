package com.anas.eventizer.data.repo

import com.anas.eventizer.data.remote.EventsFirestoreDataSource
import com.anas.eventizer.domain.models.PublicEvent
import com.anas.eventizer.domain.repo.PublicEventsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class PublicEventsRepositoryImpl @Inject constructor(
    private val eventsFirestoreDataSource: EventsFirestoreDataSource,
) :PublicEventsRepository{

    //for thread safe var caching
    private val latestPublicEventsMutex = Mutex()

    //for var caching
    private var latestPublicEvents: List<PublicEvent> = emptyList()

    override suspend fun getPublicEvents(refresh:Boolean): Flow<List<PublicEvent>> {

        if (refresh || latestPublicEvents.isEmpty()){
            return  eventsFirestoreDataSource
                .getPublicEvents()
                .onEach {
                    fetchedPublicEvents -> latestPublicEventsMutex
                    .withLock { latestPublicEvents = fetchedPublicEvents }
                }
        }

        return latestPublicEventsMutex.withLock {  flow {  emit(latestPublicEvents) }}
    }

}