package com.anas.eventizer.domain.models

import java.util.*

data class IndividualUser(
    val id:UUID,
    val name:String,
    val creationDate:String,
    val profilePic:String,
    val bookedPersonalEvents: List<PersonalEvent>,
    val bookedPublicEvents:List<PublicEvent>,
)

