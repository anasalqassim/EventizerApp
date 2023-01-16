package com.anas.eventizer.domain.useCase

import android.util.Log
import com.anas.eventizer.domain.models.PersonalEvent
import com.anas.eventizer.domain.repo.EventsRepository
import com.anas.eventizer.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

private const val TAG = "GetPersonalEventByIdUC"
class GetPersonalEventByIdUC @Inject constructor(
    private val eventsRepository: EventsRepository
){

    operator fun invoke(eventId:String): Flow<Resource<PersonalEvent?>> = channelFlow {
        try {
            send(Resource.Loading())
            eventsRepository.getPersonalEventById(eventId).collectLatest {
                Log.d(TAG, "invoke: HIiiiiii")
                send(Resource.Success(it))
            }


        }catch (e:Exception){
            send(Resource.Error(massage = e.localizedMessage ?: "something gone wrong"))
        }
    }
}