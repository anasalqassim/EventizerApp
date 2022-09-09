package com.anas.eventizer.domain.models

import java.util.*

data class EventSupporter(
    val id:UUID,
    val supportingName:String,
    val supportingCategory: String,
    val bookedOrders:List<SupporterBookingOrder>
)
