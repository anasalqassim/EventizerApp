package com.anas.eventizer.domain.models

import java.util.*

data class EventizerUser(
    val id:String,
    val name:String,
    val creationDate:String,
    val profilePic:String,
    val bookedPublicEvents:List<String>,
)

