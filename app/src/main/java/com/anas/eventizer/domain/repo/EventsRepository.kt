package com.anas.eventizer.domain.repo

interface EventsRepository : PublicEventsRepository,PersonalEventsRepository{

   suspend fun uploadEventPlaceImages(eventId:String, eventType:String)



}