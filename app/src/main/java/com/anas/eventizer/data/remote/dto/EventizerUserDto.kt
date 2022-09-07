package com.anas.eventizer.data.remote.dto

import com.anas.eventizer.domain.models.EventizerUser

data class UsersDto(
    var publicEventsId:List<String> = emptyList(),
    var isEventizer:Boolean = false
)
fun UsersDto.toEventizerUser(): EventizerUser = EventizerUser(
    publicEventsId = publicEventsId,
    isEventizer = isEventizer
)
