package com.anas.eventizer.domain.repo

import com.anas.eventizer.data.remote.dto.PersonalEventDto
import com.anas.eventizer.data.remote.dto.PublicEventDto
import com.anas.eventizer.domain.models.PersonalEvent
import com.anas.eventizer.domain.models.PublicEvent
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

interface PublicEventsRepository {
    
    suspend fun getPublicEvents(refresh:Boolean):Flow<List<PublicEvent>>

    suspend fun getPublicEventsByDate(date:Calendar):Flow<List<PublicEvent>>

    suspend fun getPublicEventsById(userId:String):Flow<List<PublicEvent>>

    suspend fun deletePublicEvent(publicEvent: PublicEvent)

    suspend fun addPublicEvent(publicEventDto: PublicEventDto)

}