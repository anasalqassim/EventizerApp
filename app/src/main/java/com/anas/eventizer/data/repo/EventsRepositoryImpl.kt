package com.anas.eventizer.data.repo

import android.graphics.Bitmap
import androidx.work.workDataOf
import com.anas.eventizer.data.remote.EventsFirestoreDataSource
import com.anas.eventizer.data.remote.LocationGoogleMapsDataSource
import com.anas.eventizer.data.remote.dto.PersonalEventDto
import com.anas.eventizer.data.remote.dto.PublicEventDto
import com.anas.eventizer.domain.models.PersonalEvent
import com.anas.eventizer.domain.models.PublicEvent
import com.anas.eventizer.domain.repo.EventsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class EventsRepositoryImpl @Inject constructor(
    private val eventsFirestoreDataSource: EventsFirestoreDataSource,
    private val externalScope: CoroutineScope,
    private val locationGoogleMapsDataSource: LocationGoogleMapsDataSource
) : EventsRepository {

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

        return latestPublicEventsMutex.withLock {  flow {  emit(latestPublicEvents) } }
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

    override suspend fun deletePublicEvent(publicEvent: PublicEvent) {
        eventsFirestoreDataSource.deletePublicEvent(publicEvent)
    }

    override suspend fun addPublicEvent(publicEventDto: PublicEventDto) {
        eventsFirestoreDataSource.addPublicEvent(publicEventDto)
    }

    private fun clearSecMinHour(date:Calendar):Calendar {
        date.clear(Calendar.SECOND)
        date.clear(Calendar.MINUTE)
        date.clear(Calendar.HOUR)
        return date
    }


    //for thread safe var caching
    private val latestPersonalEventsMutex = Mutex()

    //for var caching
    private var latestPersonalEvents: List<PersonalEvent> = emptyList()


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

    override suspend fun addPersonalEvent(personalEventDto: PersonalEventDto) {
//        personalEventDto.eventLocation.placeId?.let {
//            locationGoogleMapsDataSource.getPlaceImages(it)
//                .collect{
//                    Log.d("PersonalEventsRepositoryImpl","size = ${it.size}  list = $it")
//                }
//        }




        eventsFirestoreDataSource.addPersonalEvent(personalEventDto)
    }

}