package com.anas.eventizer.utils

sealed class Resource<T>(val data: T? = null , val massage:String? = null ){

    class Success<T>( data: T ) : Resource<T>(data)

    class Error<T>( data: T? = null , massage: String ) : Resource<T>(data , massage)

    class Loading<T>( data: T? = null) : Resource<T>(data)

}
