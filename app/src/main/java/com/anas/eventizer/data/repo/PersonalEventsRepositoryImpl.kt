package com.anas.eventizer.data.repo

import android.util.Log
import com.anas.eventizer.data.remote.EventsFirestoreDataSource
import com.anas.eventizer.data.remote.LocationGoogleMapsDataSource
import com.anas.eventizer.data.remote.dto.PersonalEventDto
import com.anas.eventizer.domain.models.PersonalEvent
import com.anas.eventizer.domain.repo.PersonalEventsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PersonalEventsRepositoryImpl @Inject constructor(
     private val  personalEventsFirestoreDataSource: EventsFirestoreDataSource,
     private val externalScope:CoroutineScope,
     private val locationGoogleMapsDataSource: LocationGoogleMapsDataSource
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

    override suspend fun deletePersonalEvent(personalEvent: PersonalEvent) =
        personalEventsFirestoreDataSource.deletePersonalEvent(personalEvent)

    override suspend fun addPersonalEvent(personalEventDto: PersonalEventDto) {
//        personalEventDto.eventLocation.placeId?.let {
//            locationGoogleMapsDataSource.getPlaceImages(it)
//                .collect{
//                    Log.d("PersonalEventsRepositoryImpl","size = ${it.size}  list = $it")
//                }
//        }

        personalEventsFirestoreDataSource.addPersonalEvent(personalEventDto)
    }


}