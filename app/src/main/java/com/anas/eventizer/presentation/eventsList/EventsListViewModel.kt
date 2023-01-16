package com.anas.eventizer.presentation.eventsList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anas.eventizer.domain.models.PersonalEvent
import com.anas.eventizer.domain.models.PublicEvent
import com.anas.eventizer.domain.useCase.GetPublicEventsUC
import com.anas.eventizer.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject

private const val TAG = "EventsListViewModel"
@HiltViewModel
class EventsListViewModel @Inject constructor(
    private val getPublicEventsUC: GetPublicEventsUC,

): ViewModel() {
    private val _publicEventsStateFlow: MutableStateFlow<PublicEventsUiState> =
        MutableStateFlow(
        PublicEventsUiState()
    )

    val publicEventsStateFlow: StateFlow<PublicEventsUiState> = _publicEventsStateFlow


    fun getPublicEvents(date:Calendar,refresh:Boolean = false){
        getPublicEventsUC(date)
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
                            val publicEventState = result.data!!.map {

                                val month = it.eventDate.getDisplayName(Calendar.MONTH,Calendar.ALL_STYLES,
                                    Locale.getDefault()
                                )
                                val day = it.eventDate.get(Calendar.DAY_OF_MONTH)
                                val date = "$month $day"

                                PublicEventItemUiState(
                                    title = it.eventName,
                                    isOwner = false,
                                    eventPicsUrl = it.eventPicsUrls,
                                    eventLocation = it.eventLocation,
                                    id = it.id,
                                    date = date
                                )
                            }
                            PublicEventsUiState(events = publicEventState)

                        }
                    }
                }

            }.launchIn(viewModelScope)
    }
}