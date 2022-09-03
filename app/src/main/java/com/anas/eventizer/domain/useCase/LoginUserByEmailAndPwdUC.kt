package com.anas.eventizer.domain.useCase

import com.anas.eventizer.domain.repo.AuthRepository
import com.anas.eventizer.utils.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUserByEmailAndPwdUC @Inject constructor(
   private val authRepository: AuthRepository
) {
    operator fun invoke(email:String,pwd:String):Flow<Resource<AuthResult>> = flow {

        try {
            emit(Resource.Loading())
            val authResult = authRepository.loginUserByEmailAndPwd(email, pwd)
            authResult.collect{
                emit(Resource.Success(data = it))
            }
        }catch (e:Exception){
            emit(Resource.Error(massage = e.localizedMessage?: "something gone wrong"))
        }

    }
}