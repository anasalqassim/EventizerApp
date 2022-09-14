package com.anas.eventizer.data.remote.dto

import java.util.Calendar
import java.util.Date

data class SupportingOrderDto(
    var supportingOrderId:String = "",
    var supportingOrderIssuerId:String = "",
    var supportId:String = "",
    var orderDate:Calendar = Calendar.Builder().setInstant(Date()).build()
)
