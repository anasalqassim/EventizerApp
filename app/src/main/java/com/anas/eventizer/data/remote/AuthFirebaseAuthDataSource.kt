package com.anas.eventizer.data.remote


import android.net.Uri
import android.security.keystore.UserNotAuthenticatedException
import com.anas.eventizer.data.remote.dto.*
import com.anas.eventizer.domain.models.User
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.CollectionReference
import com.kiwimob.firestore.coroutines.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Named

class AuthFirebaseAuthDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    @Named("usersCollection")
    private val usersCollection:CollectionReference
) {

    companion object{
        const val USERS_COLLECTION_REF = "eventizerUsers"
        const val EVENTS_SUPPORTERS_COLLECTION_REF = "eventSupporters"
    }


    suspend fun loginUserByEmailAndPwd(email:String,pwd:String):Flow<User>
        = flow {

        val user = User()
        val authResult =  firebaseAuth.signInWithEmailAndPassword(email,pwd).await()
        val databaseUser = retrieveUserInfoFromDatabase(authResult.user?.uid!!)

        user.userType = databaseUser.userType
        user.uid = authResult.user?.uid
        user.displayName = authResult.user?.displayName
        user.profilePicUrl = authResult.user?.photoUrl.toString()

        emit(user)
    }

    suspend fun registerUserByEmailAndPwd(email:String,pwd:String):Flow<AuthResult>
            = flow {
        val authResult =  firebaseAuth.createUserWithEmailAndPassword(email,pwd).await();
        emit(authResult)
    }

    /**
    Update the user displayName and/or user profile pic
     the function will throw exception if the user was not logged in
     **/
    suspend fun updateUserInfo(displayName:String? = null,photoUri: Uri? = null){
        if (firebaseAuth.currentUser != null){
            val profileReq = userProfileChangeRequest {
                if (!displayName.isNullOrEmpty()){ this.displayName = displayName}
                if (photoUri != null) {this.photoUri = photoUri}
            }
            firebaseAuth
                .currentUser!!
                .updateProfile(profileReq)
                .await()
        }else{
            throw UserNotAuthenticatedException("USER_NOT_AUTHENTICATED")
        }
    }

    private suspend fun retrieveUserInfoFromDatabase(userId:String):UsersDto{
        if (firebaseAuth.currentUser != null){
            return usersCollection
                .document(userId)
                .get()
                .await()
                .toObject(UsersDto::class.java) ?: throw UserNotFoundException("NO_DATABASE_RECORD")
        }else{
            throw UserNotAuthenticatedException("USER_NOT_AUTHENTICATED")
        }
    }

    /**
    register the user to the firestore cloud database
    the function will throw exception if the user was not logged in
     **/
    suspend fun registerUserToDatabase(user:UsersDto){
        if (firebaseAuth.currentUser != null){
            usersCollection
                .document(firebaseAuth.currentUser!!.uid)
                .set(user)
                .await()
        }else{
            throw UserNotAuthenticatedException("USER_NOT_AUTHENTICATED")
        }
    }



}