package com.anas.eventizer.domain.models

import java.util.*

data class EventSupporter(
    val id:UUID,
    val name:String,
    val creationDate: Date = Date(),
    val supportingName:String,
    val supportingCategory: String,
    val bookedOrders:List<SupporterBookingOrder>
)
