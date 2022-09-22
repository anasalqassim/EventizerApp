package com.anas.eventizer.domain.useCase

import android.util.Log
import com.anas.eventizer.domain.models.PublicEvent
import com.anas.eventizer.domain.repo.EventsRepository
import com.anas.eventizer.domain.repo.PublicEventsRepository
import com.anas.eventizer.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Calendar
import javax.inject.Inject

private const val TAG = "GetPublicEventsUC"
class GetPublicEventsUC @Inject constructor(
    private val eventsRepository: EventsRepository
) {

    operator fun invoke(date:Calendar ,refresh:Boolean=false):Flow<Resource<List<PublicEvent>>> = flow {
        try {
            emit(Resource.Loading())
            eventsRepository.getPublicEventsByDate(date)
                .collect{
                    emit(Resource.Success(data = it))
                }
        }catch (e:Exception){
            emit(Resource.Error(massage = e.localizedMessage ?: "something gone wrong"))
        }
    }

}