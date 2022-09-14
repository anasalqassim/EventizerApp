package com.anas.eventizer.data.remote

import android.content.Context
import android.security.keystore.UserNotAuthenticatedException
import android.util.Log
import com.anas.eventizer.data.EventNotOwnedByUserException
import com.anas.eventizer.data.EventsDataStore
import com.anas.eventizer.data.remote.dto.*
import com.anas.eventizer.domain.models.PersonalEvent
import com.anas.eventizer.domain.models.PublicEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.StorageReference
import com.kiwimob.firestore.coroutines.await
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
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
    private val publicEventCollection: CollectionReference,
    private val firebaseAuth: FirebaseAuth,
    private val locationGoogleMapsDataSource: LocationGoogleMapsDataSource,
    @Named("personalEventsStorageRef")
    private val personalEventStorageRef:StorageReference,
    @Named("publicEventsStorageRef")
    private val publicEventStorageRef:StorageReference,
    private val context: Context
) {


    companion object{
        const val PERSONAL_EVENT_COLLECTION_REF = "personalEvents"
        const val PUBLIC_EVENT_COLLECTION_REF = "publicEvents"

        const val PUBLIC_EVENT_KEY = "PublicEvent"
        const val PERSONAL_EVENT_KEY = "PersonalEvent"

        const val PERSONAL_EVENTS_STORAGE_REF = "personalEventsImages/"
        const val PUBLIC_EVENTS_STORAGE_REF = "publicEventsImages/"

    }

    suspend fun uploadEventImages(eventId: String,
                                  eventType: String){
        if (eventType == PUBLIC_EVENT_KEY){
            val event = publicEventCollection.document(eventId).get().await().toObject(PublicEventDto::class.java)

            event?.eventLocation?.placeId?.let {
                locationGoogleMapsDataSource.getPlaceImages(it)
                    .collect{ photosByteArray ->
                        val photosUrls = mutableListOf<String>()
                        for (photoBytes in photosByteArray){
                            val imageName = "${event.eventOwnerId}/" +
                                    "${event.eventOwnerId}${System.currentTimeMillis()}"
                            val uploadRef = publicEventStorageRef.child(imageName)

                            val uploadTask = uploadRef.putBytes(photoBytes)
                            uploadTask.await()


                            val uploadUri = uploadRef.downloadUrl.await()
                            photosUrls.add(uploadUri.toString())


                        }

                        publicEventCollection.document(eventId)
                            .set(mapOf("eventPicsUrls" to photosUrls))
                            .await()

                    }
            }
        }else{
            val event = personalEventCollection.document(eventId).get().await().toObject(PersonalEventDto::class.java)
            event?.eventLocation?.placeId?.let {
                locationGoogleMapsDataSource.getPlaceImages(it)
                    .collect{ photosByteArray ->
                        val photosUrls = mutableListOf<String>()
                        for (photoBytes in photosByteArray){
                            val imageName = "${event.eventOwnerId}/" +
                                    "${event.eventOwnerId}${System.currentTimeMillis()}"

                            val uploadRef = personalEventStorageRef.child(imageName)

                            val uploadTask = uploadRef.putBytes(photoBytes)
                            uploadTask.await()

                            val uploadUri = uploadRef.downloadUrl.await()
                            photosUrls.add(uploadUri.toString())

                        }

                        personalEventCollection.document(eventId)
                            .set(mapOf("eventPicsUrls" to photosUrls), SetOptions.merge())
                            .await()

                    }
            }

        }

    }


    /**
     * this function will throw UserNotAuthenticatedException
     * if there is no logged in user
     */
    suspend fun addPersonalEvent(personalEventDto: PersonalEventDto){
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null){
            val newDoc = personalEventCollection.document()

            personalEventDto.id = newDoc.id
            personalEventDto.eventOwnerId = currentUser.uid

            newDoc
                .set(personalEventDto)
                .await()

            EventsDataStore.setEventId(newDoc.id,context)
            EventsDataStore.setEventType(PERSONAL_EVENT_KEY,context)
            delay(2000)
        }else{
            throw UserNotAuthenticatedException("USER_NOT_AUTHENTICATED")
        }
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

    /**
     * this function will throw UserNotAuthenticatedException
     * if there is no logged in user and it will throw
     * EventNotOwnedByUserException if the current user id
     * dose not equal to the ownerId of the event
     */
    suspend fun deletePersonalEvent(personalEvent:PersonalEvent){
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null){
            if (currentUser.uid == personalEvent.eventOwnerId){
                personalEventCollection
                    .document(personalEvent.id)
                    .delete()
                    .await()
            }else{
                //TODO MAKE CONST CODE
                throw EventNotOwnedByUserException("EVENT_NOT_OWNED_BY_USER")
            }
        }else{
            //TODO MAKE CONST CODE
            throw UserNotAuthenticatedException("USER_NOT_AUTHENTICATED")
        }
    }


    /**
     * this function will throw UserNotAuthenticatedException
     * if there is no logged in user
     */
    suspend fun addPublicEvent(publicEventDto: PublicEventDto){
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null){
            val newDoc = personalEventCollection.document()
            publicEventDto.id = newDoc.id



            publicEventDto.eventOwnerId = currentUser.uid
            EventsDataStore.setEventId(newDoc.id,context)
            EventsDataStore.setEventType(PUBLIC_EVENT_KEY,context)
            newDoc
                .set(publicEventDto)
                .await()


        }else{
            throw UserNotAuthenticatedException("USER_NOT_AUTHENTICATED")
        }
    }

    suspend fun getPublicEvents():Flow<List<PublicEvent>>{

        return flow {
            withContext(ioDispatcher){
                emit(
                    publicEventCollection
                        .get()
                        .await()
                        .toObjects(PublicEventDto::class.java)
                        .map { it.toPublicEvent() }
                )
            }
        }
    }

    suspend fun getPublicEventByEventId(eventId: String):Flow<PublicEvent>{

        TODO("implement the logic")
    }

    /**
     * this function will throw UserNotAuthenticatedException
     * if there is no logged in user and it will throw
     * EventNotOwnedByUserException if the current user id
     * dose not equal to the ownerId of the event
     */
    suspend fun deletePublicEvent(publicEvent: PublicEvent){
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null){
            if (currentUser.uid == publicEvent.eventOwnerId){
                personalEventCollection
                    .document(publicEvent.id)
                    .delete()
                    .await()
            }else{
                //TODO MAKE CONST CODE
                throw EventNotOwnedByUserException("EVENT_NOT_OWNED_BY_USER")
            }
        }else{
            //TODO MAKE CONST CODE
            throw UserNotAuthenticatedException("USER_NOT_AUTHENTICATED")
        }
    }

}