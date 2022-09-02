package com.anas.eventizer.data.remote.dto

import com.anas.eventizer.domain.models.EventLocation
import com.anas.eventizer.domain.models.PublicEvent
import java.util.*

data class PublicEventDto(
    var id:String ="",
    var creationDate: Date = Date(),
    var eventName:String ="",
    var eventDate: Date = Date(),
    var eventOwnerId:String = "",
    var eventPicsUrls:List<String> = emptyList(),
    var eventLocation: EventLocationDto = EventLocationDto(0f,0f),
    var eventCategory: String = ""
)
fun PublicEventDto.toPublicEvent(): PublicEvent = PublicEvent(
    id = id,
    creationDate = creationDate,
    eventName = eventName,
    eventDate = Calendar.Builder().setInstant(eventDate).build(),
    eventOwnerId = eventOwnerId,
    eventPicsUrls = eventPicsUrls,
    eventLocation = eventLocation.toEventLocation(),
    eventCategory = eventCategory
)
