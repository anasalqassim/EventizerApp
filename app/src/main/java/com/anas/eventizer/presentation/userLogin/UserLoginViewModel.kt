package com.anas.eventizer.presentation.userLogin

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anas.eventizer.domain.models.PublicEvent
import com.anas.eventizer.domain.repo.AuthRepository
import com.anas.eventizer.domain.useCase.LoginUserByEmailAndPwdUC
import com.anas.eventizer.utils.Resource
import com.google.firebase.FirebaseError
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.FirebaseAuthKtxRegistrar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class UserLoginViewModel @Inject constructor(
    private val loginUserByEmailAndPwdUC: LoginUserByEmailAndPwdUC
): ViewModel() {
    private val _authResultStateFlow: MutableStateFlow<Resource<AuthResult>> =
        MutableStateFlow(Resource.Loading())

    val authResultStateFlow: StateFlow<Resource<AuthResult>> = _authResultStateFlow

    fun loginUserByEmailAndPwd(email:String,pwd:String){

        loginUserByEmailAndPwdUC(email,pwd).onEach{ authResult ->
            when(authResult){
                is Resource.Error -> {
                    _authResultStateFlow.value = Resource.Error(massage = authResult.massage!!)
                }
                is Resource.Loading -> {
                    _authResultStateFlow.value = Resource.Loading()
                }
                is Resource.Success -> {
                    _authResultStateFlow.value = Resource.Success(data = authResult.data!!)
                }
            }

        }.launchIn(viewModelScope)
    }

}