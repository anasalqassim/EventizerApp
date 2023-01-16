package com.anas.eventizer.domain.models

import com.anas.eventizer.presentation.calendar.toLocalDate
import java.time.LocalDate
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

fun PersonalEvent.eventDateIdPair():Pair<String,LocalDate> {

    return this.id to this.eventDate.toLocalDate()
}
