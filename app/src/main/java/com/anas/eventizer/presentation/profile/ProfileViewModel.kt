package com.anas.eventizer.presentation.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anas.eventizer.domain.models.PersonalEvent
import com.anas.eventizer.domain.useCase.GetPersonalEventsUC
import com.anas.eventizer.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

private const val TAG = "ProfileViewModel"

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getPersonalEventsUC: GetPersonalEventsUC
): ViewModel() {

    private val _personalEventsLiveData: MutableLiveData<List<PersonalEvent>> = MutableLiveData()

    val personalEventsLiveData: LiveData<List<PersonalEvent>> = _personalEventsLiveData


    fun initPersonalEvent(userId:String,refresh:Boolean = false){
        getPersonalEventsUC(userId,refresh).onEach { result->
            when (result){
                is Resource.Error ->{
                    Log.d(TAG , "oh ${result.massage}")
                }
                is Resource.Loading ->{

                }
                is Resource.Success ->{
                    Log.d(TAG , "odsjhfgsjdhfgh ${result.data}")
                }
            }

        }.launchIn(viewModelScope)
    }

}