package com.anas.eventizer.presentation.userLogin

data class LoginUiState(
    val loginResult:Boolean = false,
    val isLoading:Boolean = false,
    val errorMsg:String? = null,
    val displayName:String? = null,
    val userId:String? = null,
    val profilePicUrl:String? = null,
    val userType:String? = null
)