package com.anas.eventizer.domain.models

import com.anas.eventizer.data.remote.EventLocationDto
import com.anas.eventizer.data.remote.EventSupporterDto
import java.util.*

data class PersonalEvent(
    val id:String,
    val creationDate: Date,
    val eventName:String,
    val eventSupporters:List<String>,
    val eventDate: Date,
    val eventOwnerId:String,
    val eventPicsUrls:List<String>,
    val eventLocation: EventLocationDto,
)
