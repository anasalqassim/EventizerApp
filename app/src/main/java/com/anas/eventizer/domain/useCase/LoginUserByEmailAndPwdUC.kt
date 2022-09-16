package com.anas.eventizer.domain.useCase

import com.anas.eventizer.data.remote.UserNotFoundException
import com.anas.eventizer.domain.models.User
import com.anas.eventizer.domain.repo.AuthRepository
import com.anas.eventizer.utils.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthWebException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUserByEmailAndPwdUC @Inject constructor(
   private val authRepository: AuthRepository
) {



    operator fun invoke(email:String,pwd:String):Flow<Resource<User>> = flow {

        try {
            emit(Resource.Loading())
            val authResult = authRepository.loginUserByEmailAndPwd(email, pwd)
            authResult.collect {
                emit(Resource.Success(data = it))
            }

        }catch (e:UserNotFoundException){
            emit(Resource.Error(massage = e.localizedMessage ?: "USER_NOT_BEEN_FOUND"))
        }catch (e:FirebaseAuthInvalidUserException){
            emit(Resource.Error(massage = e.errorCode))
        }catch (e:FirebaseAuthInvalidCredentialsException){
            emit(Resource.Error(massage = e.errorCode))
        }catch (e:FirebaseAuthWebException){
            emit(Resource.Error(massage = e.errorCode))
        } catch (e:Exception){
            emit(Resource.Error(massage = "Sorry Something Gone Wrong"))
        }

    }


}