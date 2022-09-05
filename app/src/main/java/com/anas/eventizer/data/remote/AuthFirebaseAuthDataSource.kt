package com.anas.eventizer.data.remote


import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kiwimob.firestore.coroutines.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthFirebaseAuthDataSource @Inject constructor(

) {

    suspend fun loginUserByEmailAndPwd(email:String,pwd:String):Flow<AuthResult>
        = flow {
        val firebaseAuth = Firebase.auth
       val authResult =  firebaseAuth.signInWithEmailAndPassword(email,pwd)
        emit(authResult.await())
    }

    suspend fun registerUserByEmailAndPwd(email:String,pwd:String):Flow<AuthResult>
            = flow {
        val firebaseAuth = Firebase.auth
        val authResult =  firebaseAuth.createUserWithEmailAndPassword(email,pwd)
        emit(authResult.await())
    }
}