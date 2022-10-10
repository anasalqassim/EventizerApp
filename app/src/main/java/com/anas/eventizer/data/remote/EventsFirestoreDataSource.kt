package com.anas.eventizer.data.remote

import android.content.Context
import android.content.res.Resources.NotFoundException
import android.net.Uri
import android.security.keystore.UserNotAuthenticatedException
import androidx.core.net.toUri
import com.anas.eventizer.data.EventNotOwnedByUserException
import com.anas.eventizer.data.EventsDataStore
import com.anas.eventizer.data.remote.dto.*
import com.anas.eventizer.domain.models.PersonalEvent
import com.anas.eventizer.domain.models.PublicEvent
import com.anas.eventizer.domain.models.Support
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.storage.StorageReference
import com.google.type.LatLng
import com.kiwimob.firestore.coroutines.await
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
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
    private val context: Context,
    private val authDataSource: AuthFirebaseAuthDataSource,
    @Named("supportsCollection")
    private val supportsCollection: CollectionReference

) {


    companion object{
        const val PERSONAL_EVENT_COLLECTION_REF = "personalEvents"
        const val PUBLIC_EVENT_COLLECTION_REF = "publicEvents"

        const val PUBLIC_EVENT_KEY = "PublicEvent"
        const val PERSONAL_EVENT_KEY = "PersonalEvent"

        const val PERSONAL_EVENTS_STORAGE_REF = "personalEventsImages/"
        const val PUBLIC_EVENTS_STORAGE_REF = "publicEventsImages/"

    }

    suspend fun uploadEventPlaceImages(eventId: String,
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

    private suspend fun uploadEventUserTakenImages(eventId: String,
                                                   eventType: String,
                                                   eventImageURIs:List<Uri>
    ){
        if (eventType == PUBLIC_EVENT_KEY){
            //val event = publicEventCollection.document(eventId).get().await().toObject(PublicEventDto::class.java)
            val imageUrls = mutableListOf<String>()
            for (imgUri in eventImageURIs){
                val imageName = "xx1/${eventId}" +
                        "${System.currentTimeMillis()}"

                val uploadRef = publicEventStorageRef.child(imageName)
                val uploadTask = uploadRef.putFile(imgUri)
                uploadTask.await()
               imageUrls.add(uploadRef.downloadUrl.await().toString())

            }
            publicEventCollection
                .document(eventId)
                .set(mapOf("eventPicsUrls" to imageUrls), SetOptions.merge())
                .await()
        }else{
            val event = personalEventCollection.document(eventId).get().await().toObject(PersonalEventDto::class.java)
            val imageUrls = mutableListOf<String>()
            for (imgUri in eventImageURIs){
                val imageName = "${event?.eventOwnerId}/${event?.id}" +
                        "${System.currentTimeMillis()}"

                val uploadRef = personalEventStorageRef.child(imageName)
                val uploadTask = uploadRef.putFile(imgUri)
                uploadTask.await()
                imageUrls.add(uploadRef.downloadUrl.await().toString())

            }
            personalEventCollection
                .document(eventId)
                .set(mapOf("eventPicsUrls" to imageUrls), SetOptions.merge())
                .await()

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
    suspend fun addPublicEvent(publicEventDto: PublicEventDto,imageUris:List<Uri>){
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null){
            val newDoc = publicEventCollection.document()
            publicEventDto.id = newDoc.id



            publicEventDto.eventOwnerId = currentUser.uid
            EventsDataStore.setEventId(newDoc.id,context)
            EventsDataStore.setEventType(PUBLIC_EVENT_KEY,context)
            newDoc
                .set(publicEventDto)
                .await()

            uploadEventUserTakenImages(
                publicEventDto.id,
                PUBLIC_EVENT_KEY,
                imageUris
            )


        }else{
            throw UserNotAuthenticatedException("USER_NOT_AUTHENTICATED")
        }
    }

    suspend fun getPublicEvents():Flow<List<PublicEvent>>{

        return flow {
                val docs =publicEventCollection
                    .get()
                    .await()
                val publicEventsDto = mutableListOf<PublicEventDto>()
                docs.forEach { doc ->
                    val eventLocationH =  doc.get("eventLocation")!! as HashMap<String,Any>
                    val latLngH = eventLocationH["latLng"] as HashMap<String,Double>
                    val latLng = com.google.android.gms.maps.model.LatLng(latLngH["latitude"]!!,latLngH["longitude"]!!)
                    val placeId = eventLocationH["placeId"] as String?
                    val eventLocationDto = EventLocationDto(latLng = latLng, placeId = placeId)
                    val publicEventDto = PublicEventDto(
                        id = doc.get("id").toString(),
                        eventName = doc.get("eventName").toString(),
                        eventDate = doc.getDate("eventDate")!!,
                        creationDate = doc.getDate("creationDate")!!,
                        eventCategory = doc.get("eventCategory").toString(),
                        eventPicsUrls = doc.get("eventPicsUrls")!! as List<String>,
                        eventOwnerId = doc.get("eventOwnerId").toString(),
                        eventLocation = eventLocationDto
                        )
                    publicEventsDto.add(publicEventDto)
                }
                emit(
                    publicEventsDto.map { it.toPublicEvent() }
                )

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



    /**
     * this function will throw UserNotAuthenticatedException
     * if there is no logged in user and it will throw
     * EventNotOwnedByUserException if the current user id
     * dose not equal to the ownerId of the event
     * and will throw IllegalAccessException if the user type not supporter
     */
    suspend fun addEventSupport(supportDto: SupportDto){

        val currentUser = firebaseAuth.currentUser
        if (currentUser != null){
            val databaseUser = authDataSource.retrieveUserInfoFromDatabase(currentUser.uid)
            val supportDoc = supportsCollection.document()
            supportDto.supportId = supportDoc.id
            supportDto.supportOwnerId = currentUser.uid
            if (databaseUser.userType == UsersDto.UsersTypes.SUPPORTER.name){
                supportsCollection
                    .document(supportDto.supportId)
                    .set(supportDto)
                    .await()
            }else{
                //TODO MAKE CONST CODE
                throw IllegalAccessException("USER_NOT_SUPPORTER")
            }
        }else{
            //TODO MAKE CONST CODE
            throw UserNotAuthenticatedException("USER_NOT_AUTHENTICATED")
        }
    }

    /**
     * this function will throw UserNotAuthenticatedException
     * if there is no logged in user and it will throw
     * EventNotOwnedByUserException if the current user id
     * dose not equal to the ownerId of the event
     */
    suspend fun deleteSupport(support: Support){
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null){
            if (currentUser.uid == support.supportOwnerId){
                supportsCollection
                    .document(support.supportId)
                    .delete()
                    .await()
            }else{
                //TODO MAKE CONST CODE
                throw EventNotOwnedByUserException("SUPPORT_NOT_OWNED_BY_USER")
            }
        }else{
            //TODO MAKE CONST CODE
            throw UserNotAuthenticatedException("USER_NOT_AUTHENTICATED")
        }
    }

    suspend fun getSupportsByCategory(category:String):Flow<List<Support>> {
        return  flow{
          emit(supportsCollection
                .whereEqualTo("supportCategory" , category)
                .get()
                .await()
                .toObjects(SupportDto::class.java)
                .map { it.toSupport()})
        }
    }

    suspend fun getSupports():Flow<List<Support>> {
        return  flow{
            emit(supportsCollection
                .get()
                .await()
                .toObjects(SupportDto::class.java)
                .map { it.toSupport()})
        }
    }

    suspend fun getSupportById(supportId:String):Flow<Support> {
        return  flow{
            emit(supportsCollection
                .document(supportId)
                .get()
                .await()
                .toObject(SupportDto::class.java)
                ?.toSupport() ?: throw NotFoundException("SUPPORT_NOT_FOUND")
            )
        }
    }



}