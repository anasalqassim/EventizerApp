package com.anas.eventizer.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.eventsDataStore:androidx.datastore.core.DataStore<Preferences>
    by preferencesDataStore(name = "events")
object EventsDataStore {

    private const val EVENT_ID_PREF_KEY = "eventId"
    private const val EVENT_TYPE_PREF_KEY = "eventType"

    fun getEventId(context: Context):Flow<String>{
        val keyPref = stringPreferencesKey(EVENT_ID_PREF_KEY)
        return context.eventsDataStore.data
            .map {
                it[keyPref] ?: "none"
            }
    }

  suspend  fun setEventId(eventId:String , context: Context){
        val keyPref = stringPreferencesKey(EVENT_ID_PREF_KEY)
        context.eventsDataStore.edit {
            it[keyPref] = eventId
        }
    }

    fun getEventType(context: Context):Flow<String>{
        val keyPref = stringPreferencesKey(EVENT_TYPE_PREF_KEY)
        return context.eventsDataStore.data
            .map {
                it[keyPref] ?: "none"
            }
    }

    suspend  fun setEventType(eventType:String , context: Context){
        val keyPref = stringPreferencesKey(EVENT_ID_PREF_KEY)
        context.eventsDataStore.edit {
            it[keyPref] = eventType
        }




    }

}