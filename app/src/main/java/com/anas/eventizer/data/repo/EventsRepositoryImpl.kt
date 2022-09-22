package com.anas.eventizer.data.repo

import android.util.Log
import com.anas.eventizer.data.remote.EventsFirestoreDataSource
import com.anas.eventizer.data.remote.EventsTasksDataSource
import com.anas.eventizer.data.remote.dto.PersonalEventDto
import com.anas.eventizer.data.remote.dto.PublicEventDto
import com.anas.eventizer.domain.models.PersonalEvent
import com.anas.eventizer.domain.models.PublicEvent
import com.anas.eventizer.domain.repo.EventsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

private const val TAG = "EventsRepositoryImpl"
class EventsRepositoryImpl @Inject constructor(
    private val eventsFirestoreDataSource: EventsFirestoreDataSource,
    private val externalScope: CoroutineScope,
    private val eventsTasksDataSource: EventsTasksDataSource
) : EventsRepository {


    //for thread safe var caching
    private val latestPublicEventsMutex = Mutex()

    //for var caching
    private var latestPublicEvents: List<PublicEvent> = emptyList()

    //for thread safe var caching
    private val latestPersonalEventsMutex = Mutex()

    //for var caching
    private var latestPersonalEvents: List<PersonalEvent> = emptyList()



    override suspend fun uploadEventImages(
        eventId: String,
        eventType: String
    ) {
        eventsFirestoreDataSource.uploadEventImages(eventId,eventType)
    }

    override suspend fun addPublicEvent(publicEventDto: PublicEventDto) {
        eventsFirestoreDataSource.addPublicEvent(publicEventDto)

    }

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

        return latestPublicEventsMutex.withLock {  flow {  emit(latestPublicEvents) } }
    }

    override suspend fun getPublicEventsByDate(date: Calendar): Flow<List<PublicEvent>> {

        if (latestPublicEvents.isEmpty()){
            return eventsFirestoreDataSource
                .getPublicEvents()
                .map { publicEvents->
                    publicEvents.filter { publicEvent ->

                        val eventDate = clearSecMinHour(publicEvent.eventDate)

                        val wantedDate = clearSecMinHour(date)

                        eventDate[Calendar.DAY_OF_MONTH] == wantedDate[Calendar.DAY_OF_MONTH] &&
                                eventDate[Calendar.MONTH] == wantedDate[Calendar.MONTH]
                    }
                }
        }

        return  flow {
           val eventsCopy = latestPublicEvents
            eventsCopy.map { publicEvent ->
                val eventDate = clearSecMinHour(publicEvent.eventDate)

                val wantedDate = clearSecMinHour(date)

                eventDate.timeInMillis == wantedDate.timeInMillis
            }
            emit(eventsCopy)
        }

    }

    override suspend fun getPublicEventsById(userId: String): Flow<List<PublicEvent>> {
        return flow {
            eventsFirestoreDataSource
        }


    }

    override suspend fun deletePublicEvent(publicEvent: PublicEvent) {
        eventsFirestoreDataSource.deletePublicEvent(publicEvent)
    }

    private fun clearSecMinHour(date: Calendar): Calendar {
        date.set(Calendar.SECOND,0)
        date.set(Calendar.MINUTE,0)
        date.set(Calendar.HOUR_OF_DAY,0)
        return date
    }

    override suspend fun addPersonalEvent(personalEventDto: PersonalEventDto) {
        eventsFirestoreDataSource.addPersonalEvent(personalEventDto)
        eventsTasksDataSource.uploadEventImages()
    }

    override suspend fun getPersonalEvents(userId: String,refresh:Boolean): List<PersonalEvent> {

        return  if (refresh || latestPersonalEvents.isEmpty()) {
            withContext(externalScope.coroutineContext) {
                eventsFirestoreDataSource
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

    override suspend fun deletePersonalEvent(personalEvent: PersonalEvent) =
        eventsFirestoreDataSource.deletePersonalEvent(personalEvent)
}