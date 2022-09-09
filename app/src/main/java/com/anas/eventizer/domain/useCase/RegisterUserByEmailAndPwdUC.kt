package com.anas.eventizer.domain.useCase

import android.security.keystore.UserNotAuthenticatedException
import com.anas.eventizer.data.remote.dto.UsersDto
import com.anas.eventizer.domain.repo.AuthRepository
import com.anas.eventizer.utils.Resource
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class RegisterUserByEmailAndPwdUC @Inject constructor(
    private val authRepository: AuthRepository
    ){

    operator fun invoke(email:String,pwd:String,user:UsersDto):Flow<Resource<AuthResult>> = flow {

        try {
            emit(Resource.Loading())
            val registerAuthResult = authRepository.registerUserByEmailAndPwd(email, pwd, user)
            registerAuthResult.collect {
                emit(Resource.Success(data = it))
            }
        }catch (e:FirebaseAuthWeakPasswordException){
            emit(Resource.Error(massage = e.reason ?: "weak password"))
        }catch (e:FirebaseAuthEmailException){
            emit(Resource.Error(massage = e.errorCode))
        }catch (e:FirebaseAuthUserCollisionException){
            emit(Resource.Error(massage = e.errorCode))
        }catch (e:FirebaseNetworkException){
            emit(Resource.Error(massage = e.localizedMessage ?: "there was a network error" ))
        }catch (e: FirebaseAuthInvalidCredentialsException){
            emit(Resource.Error(massage = e.errorCode ))
        }catch (e:UserNotAuthenticatedException){
            emit(Resource.Error(massage = e.localizedMessage?: "there was an authentication error"))
        }

    }


    operator fun invoke(email:String,pwd:String):Flow<Resource<AuthResult>> = flow {

        try {
            emit(Resource.Loading())
            val registerAuthResult = authRepository.registerUserByEmailAndPwd(email, pwd)
            registerAuthResult.collect {
                emit(Resource.Success(data = it))
            }
        }catch (e:FirebaseAuthWeakPasswordException){
            emit(Resource.Error(massage = e.reason ?: "weak password"))
        }catch (e:FirebaseAuthEmailException){
            emit(Resource.Error(massage = e.errorCode))
        }catch (e:FirebaseAuthUserCollisionException){
            emit(Resource.Error(massage = e.errorCode))
        }catch (e:FirebaseNetworkException){
            emit(Resource.Error(massage = e.localizedMessage ?: "there was a network error" ))
        }catch (e: FirebaseAuthInvalidCredentialsException){
            emit(Resource.Error(massage = e.errorCode ))
        }catch (e:UserNotAuthenticatedException){
            emit(Resource.Error(massage = e.localizedMessage?: "there was an authentication error"))
        }

    }

}