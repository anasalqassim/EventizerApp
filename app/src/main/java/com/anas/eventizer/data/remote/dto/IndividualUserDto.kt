package com.anas.eventizer.data.remote.dto

import com.anas.eventizer.domain.models.IndividualUser
import com.anas.eventizer.utils.Constants.DATE_FORMAT
import java.util.*

data class IndividualUserDto(
    val id:UUID = UUID.randomUUID(),
    val name:String,
    val creationDate: Date = Date(),
    val profilePic:String,
    val personalEvents: List<PersonalEventDto>,
    val publicEvents:List<PublicEventDto>,
)
fun IndividualUserDto.toIndividualUser(): IndividualUser = IndividualUser(
    id = id,
    name = name,
    creationDate =
    android.text.format.DateFormat.format(DATE_FORMAT,creationDate)
        .toString(),
    profilePic = profilePic,
    bookedPersonalEvents = personalEvents.map { it
        .toPersonalEvent()},
    bookedPublicEvents = publicEvents.map { it.toPublicEvent() }
)
