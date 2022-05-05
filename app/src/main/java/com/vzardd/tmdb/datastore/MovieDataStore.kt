package com.vzardd.tmdb.datastore

import android.content.Context
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.vzardd.tmdb.util.TMDBUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

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

    suspend fun isPresent(id: Int?): Boolean {
        var s = favFlow.first()
        val obj = TMDBUtil.jsonToObject(s)
        return obj.idList.contains(id)
    }

    suspend fun addIdToFav(id: Int, context: Context){
        var s = favFlow.first()
        val obj = TMDBUtil.jsonToObject(s)
        val newList = obj.idList.toMutableList()
        newList.add(id)
        obj.idList = newList.toList()
        updateFavourites(TMDBUtil.objectToJson(obj), context)
    }

    suspend fun removeIdFromFav(id: Int, context: Context){
        var s = favFlow.first()
        val obj = TMDBUtil.jsonToObject(s)
        val newList = obj.idList.toMutableList()
        newList.remove(id)
        obj.idList = newList.toList()
        updateFavourites(TMDBUtil.objectToJson(obj), context)
    }

    suspend fun getFavListIds(): List<Int> {
        val s = favFlow.first()
        if(s.isNullOrEmpty()){
            return emptyList<Int>()
        }
        return TMDBUtil.jsonToObject(s).idList
    }
}