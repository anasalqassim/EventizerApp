package com.anas.eventizer.domain.models

data class Support(
    var supportId:String = "",
    var supportName:String = "",
    var supportCost:String = "",
    var supportCategory:String = "",
    var supportOwnerId:String = "",
    var supportPics:List<String>,
    var supportDescriptionString: String
)

