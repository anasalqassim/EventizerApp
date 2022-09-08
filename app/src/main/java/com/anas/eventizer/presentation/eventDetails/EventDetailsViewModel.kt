package com.anas.eventizer.presentation.eventDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anas.eventizer.domain.models.PersonalEvent
import com.anas.eventizer.domain.models.PublicEvent
import com.anas.eventizer.domain.useCase.DeletePersonalEventUC
import com.anas.eventizer.domain.useCase.DeletePublicEventUC
import com.anas.eventizer.utils.Resource
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    private val deletePublicEventUC: DeletePublicEventUC,
    private val deletePersonalEventUC: DeletePersonalEventUC
): ViewModel() {
    private val _deletionStateFlow: MutableStateFlow<Resource<Unit>> =
        MutableStateFlow(Resource.Loading())

    val deletionStateFlow: StateFlow<Resource<Unit>> = _deletionStateFlow



    fun deletePublicEvent(publicEvent: PublicEvent){
        deletePublicEventUC(publicEvent).onEach { result ->
            when(result){
                is Resource.Error -> {
                    _deletionStateFlow.value = Resource.Error(massage = result.massage!!)
                }
                is Resource.Loading -> {
                    _deletionStateFlow.value = Resource.Loading()
                }
                is Resource.Success -> {
                    _deletionStateFlow.value = Resource.Success(data = result.data!!)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun deletePersonalEvent(personalEvent: PersonalEvent){
        deletePersonalEventUC(personalEvent).onEach { result ->
            when(result){
                is Resource.Error -> {
                    _deletionStateFlow.value = Resource.Error(massage = result.massage!!)
                }
                is Resource.Loading -> {
                    _deletionStateFlow.value = Resource.Loading()
                }
                is Resource.Success -> {
                    _deletionStateFlow.value = Resource.Success(data = result.data!!)
                }
            }
        }.launchIn(viewModelScope)
    }
}