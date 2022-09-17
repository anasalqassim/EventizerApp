package com.anas.eventizer.data.remote.dto

import com.anas.eventizer.domain.models.User

data class UsersDto(
    var userType:String = "",
    var userBio:String = "",
){
    enum class UsersTypes{
        EVENTIZER,PERSONAL,SUPPORTER
    }
}
fun UsersDto.toEventizerUser(): User = User(
    userType = userType
)
