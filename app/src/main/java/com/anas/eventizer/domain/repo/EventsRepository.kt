package com.anas.eventizer.domain.repo

interface EventsRepository : PublicEventsRepository,PersonalEventsRepository{

    suspend fun getAllUserEventsById()

}