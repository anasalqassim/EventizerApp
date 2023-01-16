package com.anas.eventizer.presentation.eventDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anas.eventizer.domain.models.PersonalEvent
import com.anas.eventizer.domain.models.PublicEvent
import com.anas.eventizer.domain.useCase.DeletePersonalEventUC
import com.anas.eventizer.domain.useCase.DeletePublicEventUC
import com.anas.eventizer.domain.useCase.GetPersonalEventByIdUC
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
    private val getPersonalEventByIdUC: GetPersonalEventByIdUC
): ViewModel() {
    private val _personalEventStateFlow: MutableStateFlow<Resource<PersonalEvent?>> =
        MutableStateFlow(Resource.Loading())

    val personalEventStateFlow: StateFlow<Resource<PersonalEvent?>> = _personalEventStateFlow


    fun getPersonalEventById(eventId:String){
        getPersonalEventByIdUC(eventId).onEach { result->
            when(result){
                is Resource.Error -> {
                    _personalEventStateFlow.value = Resource.Error(massage = result.massage!!)
                }
                is Resource.Loading -> {
                    _personalEventStateFlow.value = Resource.Loading()
                }
                is Resource.Success -> {
                    _personalEventStateFlow.value = Resource.Success(data = result.data)
                }
            }
        }.launchIn(viewModelScope)
    }




}