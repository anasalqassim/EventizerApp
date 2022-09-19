package com.anas.eventizer.presentation.eventsList

import androidx.lifecycle.ViewModel
import com.anas.eventizer.domain.models.PersonalEvent
import com.anas.eventizer.domain.models.PublicEvent
import com.anas.eventizer.domain.useCase.GetPublicEventsUC
import com.anas.eventizer.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class EventsListViewModel @Inject constructor(
    private val getPublicEventsUC: GetPublicEventsUC,
    private val externalScope:CoroutineScope
): ViewModel() {
    private val _publicEventsStateFlow: MutableStateFlow<PublicEventsUiState> =
        MutableStateFlow(
        PublicEventsUiState()
    )

    val publicEventsStateFlow: StateFlow<PublicEventsUiState> = _publicEventsStateFlow


    fun getPublicEvents(refresh:Boolean = false){
        getPublicEventsUC(refresh)
            .onEach { result->
                when(result){
                    is Resource.Error -> {
                        _publicEventsStateFlow.update {
                            PublicEventsUiState( errorMsg = result.massage!!)
                        }
                    }
                    is Resource.Loading -> {
                        _publicEventsStateFlow.update {
                            PublicEventsUiState(isLoading = true)
                        }
                    }
                    is Resource.Success -> {
                        _publicEventsStateFlow.update {
                            val publicEvent = result.data!!.map {
                                PublicEventItemUiState(
                                    title = it.eventName,
                                    isOwner = false,
                                    eventPicsUrl = it.eventPicsUrls,
                                    eventLocation = it.eventLocation,
                                    id = it.id
                                )
                            }
                            PublicEventsUiState(events = publicEvent)

                        }
                    }
                }

            }.launchIn(externalScope)
    }
}