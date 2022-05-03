package com.vzardd.tmdb.repository

import android.util.Log
import com.vzardd.tmdb.model.MoviesList
import com.vzardd.tmdb.network.ApiService
import com.vzardd.tmdb.util.DataOrException
import java.lang.Exception
import javax.inject.Inject


class SearchRepo @Inject constructor(private val apiService: ApiService) {
    val doe: DataOrException<MoviesList?, Boolean?, Exception> = DataOrException(null, null, null)
    suspend fun getSearchMoviesList(searchKey: String): DataOrException<MoviesList?, Boolean?, Exception> {
        doe.e = null
        Log.d("SearchRepo","called")
        try{
            doe.loading = true
            doe.data = apiService.getSearchMoviesList(searchKey)
            if(doe.data.toString().isNotEmpty()){
                doe.loading = false
            }
        }catch (e: Exception){
            Log.d("SearchRepo", "error: ${e}")
            doe.e = e
        }
        doe.loading = false
        return doe
    }
}