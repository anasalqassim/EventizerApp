package com.anas.eventizer.domain.useCase

import android.security.keystore.UserNotAuthenticatedException
import com.anas.eventizer.data.EventNotOwnedByUserException
import com.anas.eventizer.domain.models.PersonalEvent
import com.anas.eventizer.domain.models.PublicEvent
import com.anas.eventizer.domain.repo.EventsRepository
import com.anas.eventizer.domain.repo.PersonalEventsRepository
import com.anas.eventizer.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeletePersonalEventUC @Inject constructor(
    private val eventsRepository: EventsRepository
) {
    operator fun invoke(personalEvent: PersonalEvent): Flow<Resource<Unit>>
            = flow {
        try {
            emit(Resource.Loading())
            emit(Resource.Success(eventsRepository.deletePersonalEvent(personalEvent)))
        }catch (e: UserNotAuthenticatedException){
            emit(Resource.Error(massage = e.localizedMessage?:"USER_NOT_AUTHENTICATED"))
        }catch (e: EventNotOwnedByUserException){
            emit(Resource.Error(massage = e.localizedMessage ?: "NOT_THE_OWNER_OF_EVENT"))
        }
    }
}