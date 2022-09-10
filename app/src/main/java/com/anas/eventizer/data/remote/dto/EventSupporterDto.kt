package com.anas.eventizer.data.remote.dto

import com.anas.eventizer.domain.models.EventSupporter
import java.util.Date
import java.util.UUID

data class EventSupporterDto(
    val id:UUID = UUID.randomUUID(),
    val supportingName:String,
    val supportingCategory: String,
    val bookedOrders:List<SupporterBookingOrderDto>
)
fun EventSupporterDto.toEventSupporter() =
    EventSupporter(
        id,
        supportingName,
        supportingCategory,
        bookedOrders.map { it.toSupporterOrder() }
    )

