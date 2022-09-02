package com.anas.eventizer.domain.useCase

import com.anas.eventizer.domain.models.PublicEvent
import com.anas.eventizer.domain.repo.PublicEventsRepository
import com.anas.eventizer.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPublicEventsUC @Inject constructor(
    private val publicEventsRepository: PublicEventsRepository
) {

    operator fun invoke(refresh:Boolean=false):Flow<Resource<List<PublicEvent>>> = flow {
        try {
            emit(Resource.Loading())
            publicEventsRepository.getPublicEvents(refresh)
                .collect{
                    emit(Resource.Success(data = it))
                }
        }catch (e:Exception){
            emit(Resource.Error(massage = e.localizedMessage ?: "something gone wrong"))
        }
    }

}