package com.anas.eventizer.data.remote.dto

import com.anas.eventizer.domain.models.EventizerUser
import com.anas.eventizer.utils.Constants.DATE_FORMAT
import java.util.*

data class UsersDto(
    var id:String = "",
    var name:String ="",
    var creationDate: Date = Date(),
    var profilePic:String = "",
    var publicEventsId:List<String> = emptyList(),
)
fun UsersDto.toEventizerUser(): EventizerUser = EventizerUser(
    id = id,
    name = name,
    creationDate =
    android.text.format.DateFormat.format(DATE_FORMAT,creationDate)
        .toString(),
    profilePic = profilePic,
    bookedPublicEvents = publicEventsId
)
