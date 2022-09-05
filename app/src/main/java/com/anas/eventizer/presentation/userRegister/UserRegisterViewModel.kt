package com.anas.eventizer.presentation.userRegister

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anas.eventizer.domain.useCase.RegisterUserByEmailAndPwdUC
import com.anas.eventizer.utils.Resource
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class UserRegisterViewModel @Inject constructor(
    private val registerUserByEmailAndPwdUC: RegisterUserByEmailAndPwdUC
) : ViewModel() {
    private val _authResultStateFlow: MutableStateFlow<Resource<AuthResult>> =
        MutableStateFlow(Resource.Loading())

    val authResultStateFlow: StateFlow<Resource<AuthResult>> = _authResultStateFlow


    fun registerUserByEmailAndPwd(email:String,pwd:String){
        registerUserByEmailAndPwdUC(email, pwd).onEach { authResult ->
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