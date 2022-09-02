package com.anas.eventizer.domain.models

import java.util.*

data class PublicEvent(
    val id:String,
    val creationDate: Date,
    val eventName:String,
    val eventDate: Calendar,
    val eventOwnerId:String,
    val eventPicsUrls:List<String>,
    val eventLocation: EventLocation,
    var eventCategory: String = ""
)
