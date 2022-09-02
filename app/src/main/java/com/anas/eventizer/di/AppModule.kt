package com.anas.eventizer.di

import com.anas.eventizer.data.remote.EventsFirestoreDataSource
import com.anas.eventizer.data.repo.PersonalEventsRepositoryImpl
import com.anas.eventizer.data.repo.PublicEventsRepositoryImpl
import com.anas.eventizer.domain.models.PersonalEvent
import com.anas.eventizer.domain.repo.PersonalEventsRepository
import com.anas.eventizer.domain.repo.PublicEventsRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
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
                                         @Named("publicEventsCollection") publicEventsCollection:CollectionReference
    ):EventsFirestoreDataSource =
        EventsFirestoreDataSource(ioDispatcher,
            personalEventsCollection,
            publicEventsCollection)


    @Provides
    @Singleton
    fun providesPersonalEventsRepository(eventsFirestoreDataSource: EventsFirestoreDataSource,coroutineScope: CoroutineScope):PersonalEventsRepository =
        PersonalEventsRepositoryImpl(eventsFirestoreDataSource,
            coroutineScope)

    @Provides
    @Singleton
    fun providesPublicEventsRepository(personalEventsFirestoreDataSource: EventsFirestoreDataSource):PublicEventsRepository =
        PublicEventsRepositoryImpl(personalEventsFirestoreDataSource,
            )

}