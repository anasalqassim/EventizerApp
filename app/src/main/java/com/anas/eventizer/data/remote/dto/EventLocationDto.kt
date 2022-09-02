package com.anas.eventizer.data.remote.dto

import com.anas.eventizer.domain.models.EventLocation

data class EventLocationDto(
    var longitude:Float = 0f,
    var latitude:Float = 0f,
)
fun EventLocationDto.toEventLocation() =
    EventLocation(
        longitude = longitude,
        latitude = latitude
    )
