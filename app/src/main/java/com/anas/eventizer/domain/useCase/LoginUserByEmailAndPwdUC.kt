package com.anas.eventizer.domain.useCase

import com.anas.eventizer.domain.repo.AuthRepository
import com.anas.eventizer.utils.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthActionCodeException
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthMultiFactorException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseAuthWebException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUserByEmailAndPwdUC @Inject constructor(
   private val authRepository: AuthRepository
) {
    enum class PwdFormatCases{
        SHORT_PASSWORD,NO_SPECIAL_CHAR,NO_CAPITAL_CHAR,VALID_PWD
    }


    operator fun invoke(email:String,pwd:String):Flow<Resource<AuthResult>> = flow {

        try {
            emit(Resource.Loading())
            val authResult = authRepository.loginUserByEmailAndPwd(email, pwd)
            authResult.collect {
                emit(Resource.Success(data = it))
            }

        } catch (e:FirebaseAuthInvalidUserException){
            emit(Resource.Error(massage = e.errorCode))
        }catch (e:FirebaseAuthInvalidCredentialsException){
            emit(Resource.Error(massage = e.errorCode))
        }catch (e:FirebaseAuthWebException){
            emit(Resource.Error(massage = e.errorCode))
        } catch (e:Exception){
            emit(Resource.Error(massage = "Sorry Something Gone Wrong"))
        }

    }
    private fun checkPwdForm(pwd: String):PwdFormatCases {

        return if (pwd.length < 4){
            PwdFormatCases.SHORT_PASSWORD
        }else if (!pwd.contains(Regex("/[`!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?~]/"))){
            PwdFormatCases.NO_SPECIAL_CHAR
        }else if (pwd.contains(Regex("[A-Z]"))){
            PwdFormatCases.NO_CAPITAL_CHAR
        }else{
            PwdFormatCases.VALID_PWD
        }
    }

}