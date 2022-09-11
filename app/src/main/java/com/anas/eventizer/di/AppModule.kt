package com.anas.eventizer.di

import android.app.Application
import com.anas.eventizer.data.remote.AuthFirebaseAuthDataSource
import com.anas.eventizer.data.remote.EventsFirestoreDataSource
import com.anas.eventizer.data.remote.LocationGoogleMapsDataSource
import com.anas.eventizer.data.repo.AuthRepositoryImpl
import com.anas.eventizer.data.repo.EventsRepositoryImpl
import com.anas.eventizer.domain.repo.AuthRepository
import com.anas.eventizer.domain.repo.EventsRepository
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @Named("personalEventsCollection")
    fun personalEventsCollection():CollectionReference =
        FirebaseFirestore
                .getInstance()
                .collection(EventsFirestoreDataSource.PERSONAL_EVENT_COLLECTION_REF)

    @Provides
    @Singleton
    @Named("publicEventsCollection")
    fun publicEventsCollection():CollectionReference =
        FirebaseFirestore
                .getInstance()
                .collection(EventsFirestoreDataSource.PUBLIC_EVENT_COLLECTION_REF)


    @Provides
    @Singleton
    @Named("ioDispatcher")
    fun providesIoCoroutineDispatcher():CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun providesIoCoroutineScope():CoroutineScope = CoroutineScope(Dispatchers.Default)


    @Provides
    @Singleton
    fun providesPersonalEventsDataSource(@Named("ioDispatcher") ioDispatcher: CoroutineDispatcher,
                                         @Named("personalEventsCollection") personalEventsCollection: CollectionReference,
                                         @Named("publicEventsCollection") publicEventsCollection:CollectionReference,
                                         firebaseAuth: FirebaseAuth,
                                         @Named("personalEventsStorageRef")
                                         personalEventStorageRef:StorageReference,
                                         @Named("publicEventsStorageRef")
                                         publicEventStorageRef:StorageReference,
                                         locationGoogleMapsDataSource: LocationGoogleMapsDataSource,
                                         context: Application
    ):EventsFirestoreDataSource =
        EventsFirestoreDataSource(ioDispatcher,
            personalEventsCollection,
            publicEventsCollection,
            firebaseAuth,
            locationGoogleMapsDataSource,
            personalEventStorageRef,
            publicEventStorageRef,
            context)



    @Provides
    @Singleton
    fun providesEventsRepository(eventsFirestoreDataSource: EventsFirestoreDataSource,
                                 coroutineScope: CoroutineScope,
                                 locationGoogleMapsDataSource: LocationGoogleMapsDataSource) : EventsRepository =
        EventsRepositoryImpl(eventsFirestoreDataSource,
            coroutineScope,
            locationGoogleMapsDataSource
        )

    @Provides
    @Singleton
    fun providesAuthFirebaseDataSource(firebaseAuth: FirebaseAuth,
                                       @Named("usersCollection")
                                       usersCollection: CollectionReference,
                                       @Named("eventSupportersCollection")
                                        eventSupportersCollection: CollectionReference
    ):AuthFirebaseAuthDataSource =
        AuthFirebaseAuthDataSource(
            firebaseAuth,
            usersCollection,
            eventSupportersCollection
        )

    @Provides
    @Singleton
    fun providesAuthRepository(authFirebaseAuthDataSource: AuthFirebaseAuthDataSource):AuthRepository =
        AuthRepositoryImpl(authFirebaseAuthDataSource)

    @Provides
    @Singleton
    fun providesFirebaseAuth():FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    @Named("usersCollection")
    fun providesFireStoreUsersCollection():CollectionReference = Firebase
        .firestore
        .collection(AuthFirebaseAuthDataSource.USERS_COLLECTION_REF)

    @Provides
    @Singleton
    @Named("eventSupportersCollection")
    fun providesFireStoreEventSupportersCollection():CollectionReference = Firebase
        .firestore
        .collection(AuthFirebaseAuthDataSource.EVENTS_SUPPORTERS_COLLECTION_REF)


    @Provides
    @Singleton
    fun providesPlacesClient(context: Application):PlacesClient =
         Places.createClient(context)

    @Provides
    @Singleton
    fun providesLocationGoogleMapsDataSource(placesClient: PlacesClient) =
        LocationGoogleMapsDataSource(placesClient)

    @Provides
    @Singleton
    fun providesFirebaseStorage() =
        Firebase.storage.reference


    @Provides
    @Singleton
    @Named("personalEventsStorageRef")
    fun providesPersonalEventStorageRef(storageRef:StorageReference) =
        storageRef.child(EventsFirestoreDataSource.PERSONAL_EVENTS_STORAGE_REF)


    @Provides
    @Singleton
    @Named("PublicEventsStorageRef")
    fun providesPublicEventStorageRef(storageRef:StorageReference) =
        storageRef.child(EventsFirestoreDataSource.PUBLIC_EVENTS_STORAGE_REF)


}