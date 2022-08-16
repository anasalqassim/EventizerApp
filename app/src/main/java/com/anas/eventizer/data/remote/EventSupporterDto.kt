package com.anas.eventizer.data.remote

import java.util.Date
import java.util.UUID

data class EventSupporterDto(
    val id:UUID = UUID.randomUUID(),
    val name:String,
    val creationDate: Date = Date(),
    val supportingName:String,
    val supportingCategory: String,
    val bookedOrders:List<SupporterBookingOrderDto>
)
