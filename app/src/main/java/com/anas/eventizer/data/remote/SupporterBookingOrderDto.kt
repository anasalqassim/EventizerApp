package com.anas.eventizer.data.remote

import com.anas.eventizer.domain.models.SupporterBookingOrder
import java.util.Date

data class SupporterBookingOrderDto(
    val supporterId: String,
    val individualUserId: String,
    val bookingDate: Date
)
fun SupporterBookingOrderDto.toSupporterOrder(): SupporterBookingOrder =
    SupporterBookingOrder(
        supporterId = supporterId,
        individualUserId = individualUserId,
        bookingDate = bookingDate
    )
