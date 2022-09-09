package com.anas.eventizer.domain.useCase

import android.security.keystore.UserNotAuthenticatedException
import com.anas.eventizer.data.remote.dto.EventSupporterDto
import com.anas.eventizer.domain.repo.AuthRepository
import com.anas.eventizer.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisterEventSupporterUC @Inject constructor(
    private val authRepository: AuthRepository
) {

    operator fun invoke(eventSupporterDto: EventSupporterDto):Flow<Resource<String>> = flow{
        try {
            emit(Resource.Loading())
            authRepository
                .registerSupporterToDatabase(eventSupporterDto)
            emit(Resource.Success("Success"))
        }catch (e:UserNotAuthenticatedException){
            emit(Resource.Error(massage = e.localizedMessage ?: "USER_NOT_AUTHENTICATED"))
        }
    }


}