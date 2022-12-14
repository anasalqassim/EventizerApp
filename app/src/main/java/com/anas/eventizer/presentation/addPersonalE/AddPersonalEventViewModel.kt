package com.anas.eventizer.presentation.addPersonalE

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anas.eventizer.data.remote.dto.PersonalEventDto
import com.anas.eventizer.data.remote.dto.PublicEventDto
import com.anas.eventizer.domain.useCase.AddPersonalEventUC
import com.anas.eventizer.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AddPersonalEventViewModel @Inject constructor(
    private val addPersonalEventUC: AddPersonalEventUC
): ViewModel() {


    private val _addingStateFlow: MutableStateFlow<Resource<Unit>> =
        MutableStateFlow(Resource.Loading())

    val addingStateFlow: StateFlow<Resource<Unit>> = _addingStateFlow


    fun addPersonalEvent(personalEventDto: PersonalEventDto){
        addPersonalEventUC(personalEventDto).onEach { result ->
            when(result){
                is Resource.Error -> {
                    _addingStateFlow.value = Resource.Error(massage = result.massage!!)
                }
                is Resource.Loading -> {
                    _addingStateFlow.value = Resource.Loading()
                }
                is Resource.Success -> {
                    _addingStateFlow.value = Resource.Success(data = result.data!!)
                }
            }
        }.launchIn(viewModelScope)
    }

}