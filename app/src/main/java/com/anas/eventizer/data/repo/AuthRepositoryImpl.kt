package com.anas.eventizer.data.repo

import com.anas.eventizer.data.remote.AuthFirebaseAuthDataSource
import com.anas.eventizer.data.remote.dto.UsersDto
import com.anas.eventizer.domain.repo.AuthRepository
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authFirebaseAuthDataSource: AuthFirebaseAuthDataSource
) : AuthRepository {
    override suspend fun loginUserByEmailAndPwd(
        email: String,
        pwd: String
    ): Flow<AuthResult> {
        return authFirebaseAuthDataSource.loginUserByEmailAndPwd(email, pwd)
    }

    override suspend fun registerUserByEmailAndPwd(
        email: String,
        pwd: String,
        user: UsersDto
    ): Flow<AuthResult> {
        return flow {
            authFirebaseAuthDataSource
                .registerUserByEmailAndPwd(email, pwd)
                .collect{
                    authFirebaseAuthDataSource
                        .registerUserToDatabase(user)
                    emit(it)
                }
        }

    }
}