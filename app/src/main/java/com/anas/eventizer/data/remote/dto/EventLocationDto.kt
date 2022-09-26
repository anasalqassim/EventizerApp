package com.anas.eventizer.data.remote.dto

import com.anas.eventizer.domain.models.EventLocation
import com.google.android.gms.maps.model.LatLng


data class EventLocationDto(
    var latLng: LatLng? = null,
    var placeId:String? = null
)
fun EventLocationDto.toEventLocation() : EventLocation =
    EventLocation(
        latLng = latLng,
        placeId = placeId
    )
