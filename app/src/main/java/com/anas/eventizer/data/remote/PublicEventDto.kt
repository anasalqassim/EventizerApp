package com.anas.eventizer.data.remote

import com.anas.eventizer.domain.models.PublicEvent
import java.util.UUID

data class PublicEventDto(
    val id:UUID = UUID.randomUUID()
)
fun PublicEventDto.toPublicEvent(): PublicEvent = PublicEvent(
    id = id
)
