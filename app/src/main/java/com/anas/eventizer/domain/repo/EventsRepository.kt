package com.anas.eventizer.domain.repo

interface EventsRepository : PublicEventsRepository,PersonalEventsRepository{

   suspend fun uploadEventPlaceImages(eventId:String, eventType:String)

   suspend fun uploadEventUserTakenImages(eventId:String, eventType:String,imageUris:Array<String>)


}