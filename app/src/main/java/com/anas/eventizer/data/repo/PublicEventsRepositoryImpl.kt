package com.anas.eventizer.data.repo

import com.anas.eventizer.data.remote.EventsFirestoreDataSource
import com.anas.eventizer.domain.models.PublicEvent
import com.anas.eventizer.domain.repo.PublicEventsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.*
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
                        fetchedPublicEvents ->
                    latestPublicEventsMutex.withLock{
                        latestPublicEvents = fetchedPublicEvents }
                }
        }

        return latestPublicEventsMutex.withLock {  flow {  emit(latestPublicEvents) }}
    }

    override suspend fun getPublicEventsByDate(date: Calendar): Flow<List<PublicEvent>> {

        if (latestPublicEvents.isEmpty()){
            return   eventsFirestoreDataSource
                .getPublicEvents()
                .onEach { publicEvents->
                    publicEvents.filter { publicEvent ->

                        val eventDate = clearSecMinHour(publicEvent.eventDate)

                        val wantedDate = clearSecMinHour(date)

                       eventDate.timeInMillis == wantedDate.timeInMillis
                    }
                }
        }

        return  flow {
            val filteredEvents = latestPublicEvents.filter { publicEvent ->
                val eventDate = clearSecMinHour(publicEvent.eventDate)

                val wantedDate = clearSecMinHour(date)

                eventDate.timeInMillis == wantedDate.timeInMillis
            }
            emit(filteredEvents)
        }

    }

    override suspend fun getPublicEventsById(userId: String): Flow<List<PublicEvent>> {
        TODO("Not yet implemented")


    }

    private fun clearSecMinHour(date:Calendar):Calendar {
        date.clear(Calendar.SECOND)
        date.clear(Calendar.MINUTE)
        date.clear(Calendar.HOUR)
        return date
    }


}