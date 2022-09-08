package com.anas.eventizer.domain.useCase

import com.anas.eventizer.domain.models.PersonalEvent
import com.anas.eventizer.domain.repo.PersonalEventsRepository
import com.anas.eventizer.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPersonalEventsUC @Inject constructor(
    private val personaEventsRepositoryImpl: PersonalEventsRepository
) {

     operator fun invoke(userId:String,refresh:Boolean):Flow<Resource<List<PersonalEvent>>> = flow {
         try {
             emit(Resource.Loading<List<PersonalEvent>>())
             val personalEvents = personaEventsRepositoryImpl.getPersonalEvents(userId,refresh)
             emit(Resource.Success<List<PersonalEvent>>(personalEvents))

         }catch (e:Exception){
             emit(Resource.Error<List<PersonalEvent>>(massage = e.localizedMessage ?: "something gone wrong"))
         }
     }
}