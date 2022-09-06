package com.anas.eventizer.data.remote


import com.anas.eventizer.data.remote.dto.UsersDto
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kiwimob.firestore.coroutines.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthFirebaseAuthDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val usersCollection:CollectionReference,

) {

    suspend fun loginUserByEmailAndPwd(email:String,pwd:String):Flow<AuthResult>
        = flow {
        val firebaseAuth = Firebase.auth
       val authResult =  firebaseAuth.signInWithEmailAndPassword(email,pwd)
        emit(authResult.await())
    }

    suspend fun registerUserByEmailAndPwd(email:String,pwd:String):Flow<AuthResult>
            = flow {

        val authResult =  firebaseAuth.createUserWithEmailAndPassword(email,pwd).await()

        emit(authResult)
    }

//    suspend fun updateUserDisplayName(userId:String,displayName:String){
//         usersCollection
//             .document(userId)
//             .set(mapOf("displayName"))
//             .await()
//    }
}