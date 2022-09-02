package com.anas.eventizer.domain.repo

import com.anas.eventizer.domain.models.PublicEvent
import kotlinx.coroutines.flow.Flow

interface PublicEventsRepository {
    
    suspend fun getPublicEvents(refresh:Boolean):Flow<List<PublicEvent>>
    
}