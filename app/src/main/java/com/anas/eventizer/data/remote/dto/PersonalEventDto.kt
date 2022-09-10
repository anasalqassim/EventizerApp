package com.anas.eventizer.data.remote.dto

import com.anas.eventizer.domain.models.PersonalEvent
import java.util.*

data class PersonalEventDto(
    var id: String  = "",
    var creationDate:Date = Date(),
    var eventName:String ="",
    var eventSupporters:List<String> = emptyList(),
    var eventDate: Date = Date(),
    var eventOwnerId:String = "",
    var eventPicsUrls:List<String> = emptyList(),
    var eventLocation:EventLocationDto = EventLocationDto(),
    var eventCategory: String = ""
)
fun PersonalEventDto.toPersonalEvent():PersonalEvent = PersonalEvent(
    id,
    creationDate ,
    eventName ,
    eventSupporters ,
    eventDate ,
    eventOwnerId ,
    eventPicsUrls ,
    eventLocation.toEventLocation(),
    eventCategory
)
