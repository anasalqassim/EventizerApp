package com.anas.eventizer.presentation.eventsList


data class PublicEventsUiState(
    val isLoading:Boolean = false,
    val events:List<PublicEventItemUiState> = emptyList(),
)

data class PublicEventItemUiState(
    val isLoading:Boolean = false,
    val title:String = "",
    val isOwner:Boolean = false,
    val eventPicsUrl:List<String> = emptyList(),

)
