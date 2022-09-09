package com.anas.eventizer.data.remote

import android.security.keystore.UserNotAuthenticatedException
import com.anas.eventizer.data.EventNotOwnedByUserException
import com.anas.eventizer.data.remote.dto.*
import com.anas.eventizer.domain.models.PersonalEvent
import com.anas.eventizer.domain.models.PublicEvent
import com.google.firebase.auth.FirebaseAuth
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
    private val publicEventCollection: CollectionReference,

    private val firebaseAuth: FirebaseAuth

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
    suspend fun addPersonalEvent(personalEventDto: PersonalEventDto){
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null){
            val newDoc = personalEventCollection.document()
            personalEventDto.id = newDoc.id
            personalEventDto.eventOwnerId = currentUser.uid
            newDoc
                .set(personalEventDto)
                .await()
        }else{
            throw UserNotAuthenticatedException("USER_NOT_AUTHENTICATED")
        }
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
            newDoc
                .set(publicEventDto)
                .await()
        }else{
            throw UserNotAuthenticatedException("USER_NOT_AUTHENTICATED")
        }
    }

}