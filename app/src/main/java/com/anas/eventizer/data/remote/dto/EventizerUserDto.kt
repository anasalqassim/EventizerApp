package com.anas.eventizer.data.remote.dto

import com.anas.eventizer.domain.models.EventizerUser
import java.util.UUID

data class EventizerUserDto(
    val id:UUID = UUID.randomUUID()
)
fun EventizerUserDto.toEventizerUser():EventizerUser = EventizerUser(
    id = id
)

