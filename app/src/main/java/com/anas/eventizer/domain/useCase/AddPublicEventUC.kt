package com.anas.eventizer.domain.useCase

import android.net.Uri
import android.security.keystore.UserNotAuthenticatedException
import com.anas.eventizer.data.remote.dto.PersonalEventDto
import com.anas.eventizer.data.remote.dto.PublicEventDto
import com.anas.eventizer.domain.repo.EventsRepository
import com.anas.eventizer.domain.repo.PublicEventsRepository
import com.anas.eventizer.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddPublicEventUC @Inject constructor(
    private val eventsRepository: EventsRepository
) {

    operator fun invoke(publicEventDto: PublicEventDto,imagesUri:List<Uri>): Flow<Resource<Unit>>
            = flow {
        try {
            emit(Resource.Loading())
            emit(Resource.Success(eventsRepository
                .addPublicEvent(publicEventDto,imagesUri)))
        }catch (e: UserNotAuthenticatedException){
            emit(Resource.Error(massage = e.localizedMessage?:"USER_NOT_AUTHENTICATED"))
        }
    }
}