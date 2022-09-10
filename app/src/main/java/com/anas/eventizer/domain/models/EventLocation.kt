package com.anas.eventizer.domain.models

import com.google.type.LatLng

data class EventLocation(
    var latLng: LatLng? ,
    var placeId:String?
)
