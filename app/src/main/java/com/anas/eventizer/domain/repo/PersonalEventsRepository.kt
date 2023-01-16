package com.anas.eventizer.domain.repo

import com.anas.eventizer.data.remote.dto.PersonalEventDto
import com.anas.eventizer.domain.models.PersonalEvent
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface PersonalEventsRepository {

   suspend fun getPersonalEvents(userId:String,refresh:Boolean):List<PersonalEvent>

   suspend fun getPersonalEventByDate(userId:String,localDate: LocalDate):PersonalEvent

   suspend fun getPersonalEventById(eventId:String):Flow<PersonalEvent?>

   suspend fun deletePersonalEvent(personalEvent: PersonalEvent)

   suspend fun addPersonalEvent(personalEventDto: PersonalEventDto)



}