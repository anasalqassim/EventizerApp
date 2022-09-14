package com.anas.eventizer.data.remote.dto

import com.anas.eventizer.domain.models.Support

data class SupportDto(
    var supportId:String = "",
    var supportName:String = "",
    var supportCost:String = "",
    var supportCategory:String = "",
    var supportOwnerId:String = "",
    var supportPics:List<String> = emptyList(),
    var supportDescriptionString: String = ""
)

fun SupportDto.toSupport():Support =
    Support(
     supportId,
     supportName,
     supportCost,
     supportCategory,
     supportOwnerId,
     supportPics,
     supportDescriptionString
    )

