package com.anas.eventizer.domain.useCase

import android.security.keystore.UserNotAuthenticatedException
import com.anas.eventizer.data.remote.dto.PersonalEventDto
import com.anas.eventizer.data.remote.dto.PublicEventDto
import com.anas.eventizer.domain.repo.PublicEventsRepository
import com.anas.eventizer.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddPublicEventUC @Inject constructor(
    private val publicEventsRepository: PublicEventsRepository
) {

    operator fun invoke(publicEventDto: PublicEventDto): Flow<Resource<Unit>>
            = flow {
        try {
            emit(Resource.Loading())
            emit(Resource.Success(publicEventsRepository.addPublicEvent(publicEventDto)))
        }catch (e: UserNotAuthenticatedException){
            emit(Resource.Error(massage = e.localizedMessage?:"USER_NOT_AUTHENTICATED"))
        }
    }
}