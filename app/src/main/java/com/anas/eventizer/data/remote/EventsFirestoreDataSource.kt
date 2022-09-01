package com.anas.eventizer.data.remote

import com.anas.eventizer.domain.models.PersonalEvent
import com.google.firebase.firestore.CollectionReference
import com.kiwimob.firestore.coroutines.await
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class EventsFirestoreDataSource @Inject constructor(
    @Named("ioDispatcher")
    val ioDispatcher: CoroutineDispatcher,
    @Named("personalEventsCollection")
    val personalEventCollection: CollectionReference
) {

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
}