package com.anas.eventizer.domain.repo

interface EventsRepository : PublicEventsRepository,PersonalEventsRepository{

   suspend fun uploadEventImages(eventId:String,eventType:String)


}