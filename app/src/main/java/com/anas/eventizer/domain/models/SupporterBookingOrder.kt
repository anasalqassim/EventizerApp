package com.anas.eventizer.domain.models

import java.util.*

data class SupporterBookingOrder(
    val supporterId: String,
    val individualUserId: String,
    val bookingDate: Date
)
