package com.anas.eventizer.presentation.eventsList

import com.anas.eventizer.domain.models.EventLocation


data class PublicEventsUiState(
    val isLoading:Boolean = false,
    val errorMsg:String = "",
    val events:List<PublicEventItemUiState> = emptyList(),
)

data class PublicEventItemUiState(
    val title:String = "",
    val isOwner:Boolean = false,
    val eventPicsUrl:List<String> = emptyList(),
    val eventLocation: EventLocation,
    val id:String,
)
