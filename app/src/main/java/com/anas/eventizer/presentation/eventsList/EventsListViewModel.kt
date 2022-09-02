package com.anas.eventizer.presentation.eventsList

import androidx.lifecycle.ViewModel
import com.anas.eventizer.domain.models.PersonalEvent
import com.anas.eventizer.domain.models.PublicEvent
import com.anas.eventizer.domain.useCase.GetPublicEventsUC
import com.anas.eventizer.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class EventsListViewModel @Inject constructor(
    private val getPublicEventsUC: GetPublicEventsUC,
    private val externalScope:CoroutineScope
): ViewModel() {
    private val _publicEventsStateFlow: MutableStateFlow<Resource<List<PublicEvent>>> =
        MutableStateFlow(
        Resource.Loading()
    )

    val publicEventsStateFlow: StateFlow<Resource<List<PublicEvent>>> = _publicEventsStateFlow


    fun getPublicEvents(refresh:Boolean = false){
        getPublicEventsUC(refresh)
            .onEach { result->
                when(result){
                    is Resource.Error -> {
                        _publicEventsStateFlow.value = Resource.Error(massage = result.massage!!)
                    }
                    is Resource.Loading -> {
                        _publicEventsStateFlow.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _publicEventsStateFlow.value = Resource.Success(data = result.data!!)
                    }
                }

            }.launchIn(externalScope)
    }
}