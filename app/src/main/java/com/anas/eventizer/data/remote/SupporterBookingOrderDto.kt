package com.anas.eventizer.data.remote

import java.util.Date

data class SupporterBookingOrderDto(
    val supporterId: String,
    val individualUserId: String,
    val bookingDate: Date
)
