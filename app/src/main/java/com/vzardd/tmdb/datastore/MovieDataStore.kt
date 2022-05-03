package com.vzardd.tmdb.datastore

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

private val PREF_NAME = "favourite_movies"

private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = PREF_NAME)

class MovieDataStore(context: Context) {

    //key
    private val favourites = stringPreferencesKey("favourites")

    //to read
    val favFlow : Flow<String> = context.dataStore.data.map {
        it[favourites]?:""
    }

    //to write
    suspend fun updateFavourites(value: String, context: Context){
        context.dataStore.edit {
            it[favourites] = value
        }
    }
}