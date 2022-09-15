package com.anas.eventizer.presentation.userRegister

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anas.eventizer.data.remote.dto.UsersDto
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

    private val _registerUiStateFlow: MutableStateFlow<RegisterUiState> =
        MutableStateFlow(RegisterUiState())

    val authResultStateFlow: StateFlow<RegisterUiState> = _registerUiStateFlow


    fun registerUserByEmailAndPwd(email:String,pwd:String,user:UsersDto){
        registerUserByEmailAndPwdUC(email, pwd, user).onEach { authResult ->
            when(authResult){
                is Resource.Error -> {
                    _registerUiStateFlow.update {
                        RegisterUiState(errorMsg = authResult.massage)
                    }

                }
                is Resource.Loading -> {
                    _registerUiStateFlow.update { RegisterUiState(isLoading = true) }
                }
                is Resource.Success -> {
                    _registerUiStateFlow.update {
                        RegisterUiState(
                            registerResult = true,
                            displayName = authResult.data?.user?.displayName,
                            profilePic = authResult.data?.user?.photoUrl.toString(),
                            userType = user.userType
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

}