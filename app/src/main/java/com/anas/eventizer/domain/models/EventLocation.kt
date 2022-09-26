package com.anas.eventizer.domain.models

import com.google.android.gms.maps.model.LatLng

data class EventLocation(
    var latLng: LatLng? = null ,
    var placeId:String? = null
)
