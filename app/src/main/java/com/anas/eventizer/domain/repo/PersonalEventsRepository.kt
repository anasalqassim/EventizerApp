package com.anas.eventizer.domain.repo

import com.anas.eventizer.data.remote.dto.PersonalEventDto
import com.anas.eventizer.domain.models.PersonalEvent

interface PersonalEventsRepository {

   suspend fun getPersonalEvents(userId:String,refresh:Boolean):List<PersonalEvent>

   suspend fun deletePersonalEvent(personalEvent: PersonalEvent)

   suspend fun addPersonalEvent(personalEventDto: PersonalEventDto)



}