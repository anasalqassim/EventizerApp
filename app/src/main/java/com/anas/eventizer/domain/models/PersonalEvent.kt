package com.anas.eventizer.domain.models

import java.util.*

data class PersonalEvent(
    val id:String,
    val creationDate: Date,
    val eventName:String,
    val eventSupporters:List<String>,
    val eventDate: Date,
    val eventOwnerId:String,
    val eventPicsUrls:List<String>,
    val eventLocation: EventLocation,
    var eventCategory: String
)
