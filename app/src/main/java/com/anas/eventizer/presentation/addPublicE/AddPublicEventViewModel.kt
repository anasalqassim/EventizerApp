package com.anas.eventizer.presentation.addPublicE

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anas.eventizer.data.remote.dto.PublicEventDto
import com.anas.eventizer.domain.useCase.AddPublicEventUC
import com.anas.eventizer.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

private const val TAG = "AddPublicEventViewModel"

@HiltViewModel
class AddPublicEventViewModel @Inject constructor(
    private val addPublicEventUC: AddPublicEventUC,
    private val state: SavedStateHandle

): ViewModel() {

    private val _addingStateFlow: MutableStateFlow<Resource<Unit>> =
        MutableStateFlow(Resource.Loading())

    val addingStateFlow: StateFlow<Resource<Unit>> = _addingStateFlow


    fun addPublicEvent(publicEventDto: PublicEventDto,imageUris:List<Uri>){
        addPublicEventUC(publicEventDto,imageUris).onEach { result ->
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


//    fun saveEventFields(title:String,date:Long,categoryPos:Int,imagesUrIs:List<Uri>){
//        state["title"] = title
//        state["date"] = date
//        state["categoryPos"] = categoryPos
//        state["imagesUrIs"] = imagesUrIs
//        Log.d(TAG, "saveEventFields: ${state.get<String>("title")}")
//
//    }
//    fun getEventTitle():String? {return state.get<String>("title") }

}