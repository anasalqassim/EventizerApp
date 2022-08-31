package com.anas.eventizer.di

import com.anas.eventizer.data.remote.EventsFirestoreDataSource
import com.anas.eventizer.data.repo.PersonalEventsRepositoryImpl
import com.anas.eventizer.domain.models.PersonalEvent
import com.anas.eventizer.domain.repo.PersonalEventsRepository
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
    fun providesFirestore():CollectionReference = FirebaseFirestore.getInstance().collection("personalEvents")

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
                                         @Named("personalEventsCollection") personalEventsCollection: CollectionReference):EventsFirestoreDataSource =
        EventsFirestoreDataSource(ioDispatcher,personalEventsCollection)


    @Provides
    @Singleton
    fun providesPersonalEventsRepository(personalEventsFirestoreDataSource: EventsFirestoreDataSource,coroutineScope: CoroutineScope):PersonalEventsRepository =
        PersonalEventsRepositoryImpl(personalEventsFirestoreDataSource,coroutineScope)

}