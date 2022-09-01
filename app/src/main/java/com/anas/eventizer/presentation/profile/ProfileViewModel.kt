package com.anas.eventizer.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anas.eventizer.domain.models.PersonalEvent
import com.anas.eventizer.domain.useCase.GetPersonalEventsUC
import com.anas.eventizer.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

//private const val TAG = "ProfileViewModel"

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getPersonalEventsUC: GetPersonalEventsUC
): ViewModel() {

    private val _personalEventsStateFlow: MutableStateFlow<Resource<List<PersonalEvent>>> = MutableStateFlow(
        Resource.Success(emptyList())
    )

    val personalEventsLiveData: StateFlow<Resource<List<PersonalEvent>>> = _personalEventsStateFlow


    fun initPersonalEvent(userId:String,refresh:Boolean = false){
        getPersonalEventsUC(userId,refresh).onEach { result->
            when (result){
                is Resource.Error ->{
                    _personalEventsStateFlow.value = result.massage?.let { Resource.Error(massage = it) }!!
                }
                is Resource.Loading ->{
                    _personalEventsStateFlow.value = Resource.Loading()
                }
                is Resource.Success ->{
                    _personalEventsStateFlow.value = Resource.Success(data = result.data!!)
                }
            }

        }.launchIn(viewModelScope)
    }

}