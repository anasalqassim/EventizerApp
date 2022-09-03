package com.anas.eventizer.data.repo

import com.anas.eventizer.data.remote.AuthFirebaseAuthDataSource
import com.anas.eventizer.domain.repo.AuthRepository
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authFirebaseAuthDataSource: AuthFirebaseAuthDataSource
) : AuthRepository {
    override suspend fun loginUserByEmailAndPwd(
        email: String,
        pwd: String
    ): Flow<AuthResult> {
        //TODO: implement the logic of checking the email  and password form
        return authFirebaseAuthDataSource.loginUserByEmailAndPwd(email, pwd)
    }
}