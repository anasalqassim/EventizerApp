package com.anas.eventizer.domain.repo

import com.anas.eventizer.data.remote.dto.UsersDto
import com.anas.eventizer.domain.models.User
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun loginUserByEmailAndPwd(email:String,pwd:String): Flow<User>

    suspend fun registerUserByEmailAndPwd(email:String,pwd:String,user: UsersDto): Flow<AuthResult>

    suspend fun registerUserByEmailAndPwd(email:String,pwd:String): Flow<AuthResult>


}