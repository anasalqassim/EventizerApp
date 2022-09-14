package com.anas.eventizer.data.remote.dto

import java.util.Calendar
import java.util.Date

data class EventOrderDto(
    var eventType:String = "",
    var eventOrderIssuerId:String = "",
    var orderedEventId:String,
    var orderDate: Calendar = Calendar.Builder().setInstant(Date()).build()
)


