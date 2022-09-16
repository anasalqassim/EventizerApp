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
    private val _loginUiStateFlow: MutableStateFlow<LoginUiState> =
        MutableStateFlow(LoginUiState())

    val loginUiStateFlow: StateFlow<LoginUiState> = _loginUiStateFlow

    fun loginUserByEmailAndPwd(email:String,pwd:String){

        loginUserByEmailAndPwdUC(email,pwd).onEach{ authResult ->
            when(authResult){
                is Resource.Error -> {
                    _loginUiStateFlow.update {   LoginUiState(errorMsg = authResult.massage )}
                }
                is Resource.Loading -> {
                    _loginUiStateFlow.update {   LoginUiState(isLoading = true)}
                }
                is Resource.Success -> {

                    _loginUiStateFlow.update {   LoginUiState(
                        loginResult = true,
                        displayName = authResult.data?.displayName,
                        profilePicUrl = authResult.data?.profilePicUrl.toString(),
                        userId = authResult.data?.uid,
                        userType = authResult.data?.userType,
                    )}
                }
            }

        }.launchIn(viewModelScope)
    }

}