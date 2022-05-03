package com.vzardd.tmdb.repository

import com.vzardd.tmdb.model.Credits
import com.vzardd.tmdb.model.FullMovieDetails
import com.vzardd.tmdb.model.KeywordsList
import com.vzardd.tmdb.model.MoviesList
import com.vzardd.tmdb.network.ApiService
import com.vzardd.tmdb.util.DataOrException
import java.lang.Exception
import javax.inject.Inject

class MovieRepo @Inject constructor(private val apiService: ApiService) {
    val doe = DataOrException<FullMovieDetails,Boolean,Exception>()
    val cdoe = DataOrException<Credits,Boolean,Exception>()
    val kdoe = DataOrException<KeywordsList,Boolean,Exception>()
    val rdoe = DataOrException<MoviesList,Boolean,Exception>()
    val tdoe = DataOrException<MoviesList,Boolean,Exception>()
    suspend fun getMovieDetails(id: Int) : DataOrException<FullMovieDetails,Boolean,Exception>{
        try{
            doe.e = null
            doe.loading = true
            doe.data = apiService.getMovieDetails(id)
            if(doe.data!=null){
                doe.loading = false
            }
        }catch (e:Exception){
            doe.e = e
        }
        doe.loading = false
        return doe
    }

    suspend fun getCredits(id: Int) : DataOrException<Credits,Boolean,Exception>{
        try{
            cdoe.e = null
            cdoe.loading = true
            cdoe.data = apiService.getMovieCredits(id)
            if(cdoe.data!=null){
                cdoe.loading = false
            }
        }catch (e:Exception){
            cdoe.e = e
        }
        cdoe.loading = false
        return cdoe
    }

    suspend fun getKeywords(id: Int) : DataOrException<KeywordsList,Boolean,Exception>{
        try{
            kdoe.e = null
            kdoe.loading = true
            kdoe.data = apiService.getMovieKeywords(id)
            if(kdoe.data!=null){
                kdoe.loading = false
            }
        }catch (e:Exception){
            kdoe.e = e
        }
        kdoe.loading = false
        return kdoe
    }

    suspend fun getRecommendations(id: Int) : DataOrException<MoviesList,Boolean,Exception>{
        try{
            rdoe.e = null
            rdoe.loading = true
            rdoe.data = apiService.getMovieRecommendations(id)
            if(rdoe.data!=null){
                rdoe.loading = false
            }
        }catch (e:Exception){
            rdoe.e = e
        }
        rdoe.loading = false
        return rdoe
    }

    suspend fun getMoviesByCategory(category: String) : DataOrException<MoviesList,Boolean,Exception>{
        try{
            tdoe.e = null
            tdoe.loading = true
            tdoe.data = apiService.getMoviesByCategory(category)
            if(tdoe.data!=null){
                tdoe.loading = false
            }
        }catch (e:Exception){
            tdoe.e = e
        }
        tdoe.loading = false
        return tdoe
    }
}