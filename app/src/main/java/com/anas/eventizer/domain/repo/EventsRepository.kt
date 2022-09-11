package com.anas.eventizer.domain.repo

interface EventsRepository : PublicEventsRepository,PersonalEventsRepository{

   suspend fun uploadEventImages(images:List<ByteArray>,eventId:String,eventType:String)


}