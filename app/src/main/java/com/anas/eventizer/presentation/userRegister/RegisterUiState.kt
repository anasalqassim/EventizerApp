package com.anas.eventizer.presentation.userRegister

data class RegisterUiState(
    val registerResult:Boolean = false,
    val isLoading:Boolean = false,
    val errorMsg:String? = null,
    val displayName:String? = null,
    val profilePic:String? = null,
    val userType:String? = null
)
