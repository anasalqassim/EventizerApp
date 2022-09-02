package com.anas.eventizer.data.remote

import com.anas.eventizer.data.remote.dto.PersonalEventDto
import com.anas.eventizer.data.remote.dto.PublicEventDto
import com.anas.eventizer.data.remote.dto.toPersonalEvent
import com.anas.eventizer.data.remote.dto.toPublicEvent
import com.anas.eventizer.domain.models.PersonalEvent
import com.anas.eventizer.domain.models.PublicEvent
import com.google.firebase.firestore.CollectionReference
import com.kiwimob.firestore.coroutines.await
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date
import javax.inject.Inject
import javax.inject.Named

class EventsFirestoreDataSource @Inject constructor(
    @Named("ioDispatcher")
    private val ioDispatcher: CoroutineDispatcher,
    @Named("personalEventsCollection")
    private val personalEventCollection: CollectionReference,
    @Named("publicEventsCollection")
    private val publicEventCollection: CollectionReference

) {
    companion object{
        const val PERSONAL_EVENT_COLLECTION_REF = "personalEvents"
        const val PUBLIC_EVENT_COLLECTION_REF = "publicEvents"
    }

    suspend fun getPersonalEvents(userId:String):List<PersonalEvent> {
        return  withContext(ioDispatcher){
            personalEventCollection
                .whereEqualTo("eventOwnerId" , userId)
                .get()
                .await()
                .toObjects(PersonalEventDto::class.java)
                .map { it.toPersonalEvent() }
        }
    }

    suspend fun getPublicEvents():Flow<List<PublicEvent>>{

        return flow {
               emit( publicEventCollection
                    .get()
                    .await()
                    .toObjects(PublicEventDto::class.java)
                    .map { it.toPublicEvent() }
               )
        }
    }
}