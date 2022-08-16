package com.anas.eventizer.data.remote

import com.anas.eventizer.domain.models.PersonalEvent
import java.util.Date
import java.util.UUID

data class PersonalEventDto(
    val id:UUID = UUID.randomUUID(),
    val creationDate:Date = Date(),
    val name:String,
    val eventSupporters:List<EventSupporterDto>
)
fun PersonalEventDto.toPersonalEvent():PersonalEvent = PersonalEvent(
    id = id
)
