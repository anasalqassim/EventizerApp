package com.anas.eventizer.domain.repo

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun loginUserByEmailAndPwd(email:String,pwd:String): Flow<AuthResult>

}