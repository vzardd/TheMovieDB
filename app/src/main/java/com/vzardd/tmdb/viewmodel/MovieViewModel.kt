package com.vzardd.tmdb.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vzardd.tmdb.model.*
import com.vzardd.tmdb.repository.MovieRepo
import com.vzardd.tmdb.util.DataOrException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val movieRepo: MovieRepo): ViewModel() {
    val movieDetails = mutableStateOf(DataOrException<FullMovieDetails?,Boolean?,Exception>())
    val movieCredits = mutableStateOf(DataOrException<Credits?,Boolean?,Exception>())
    val movieKeywords = mutableStateOf(DataOrException<KeywordsList?,Boolean?,Exception>())
    val movieRecommendations = mutableStateOf(DataOrException<MoviesList?,Boolean?,Exception>())
    //category wise
    val popularMovies = mutableStateOf(DataOrException<MoviesList?,Boolean?,Exception>())
    val topRatedMovies = mutableStateOf(DataOrException<MoviesList?,Boolean?,Exception>())
    val nowPlayingMovies = mutableStateOf(DataOrException<MoviesList?,Boolean?,Exception>())
    val upcomingMovies = mutableStateOf(DataOrException<MoviesList?,Boolean?,Exception>())

    init {
        getPopularMovies()
        getTopRatedMovies()
        getNowPlayingMovies()
        getUpcomingMovies()
    }

    fun getMovieDetails(id: Int){
        Log.d("MovieViewModel","called")
        movieDetails.value = DataOrException(FullMovieDetails(),true,null)
        viewModelScope.launch {
            Log.d("SearchViewModel", "saved")
            val doe = DataOrException<FullMovieDetails?,Boolean?,Exception>(null,true,null)
            try{
                doe.e = null
                movieDetails.value.loading = true
                doe.loading = true;
                val res = movieRepo.getMovieDetails(id)
                doe.data = res.data
                doe.e = res.e
                if(doe.data != null){
                    doe.loading = false
                }
            }catch (e:Exception){
                doe.e = e
                Log.d("SearchViewModel","error: $e")
            }
            doe.loading = false
            movieDetails.value = doe
        }
        Log.d("SearchViewModel","completed")
    }

    fun getMovieCredits(id: Int){
        Log.d("MovieViewModel","called")
        movieCredits.value = DataOrException(Credits(),true,null)
        viewModelScope.launch {
            Log.d("SearchViewModel", "saved")
            val doe = DataOrException<Credits?,Boolean?,Exception>(null,true,null)
            try{
                doe.e = null
                movieCredits.value.loading = true
                doe.loading = true;
                val res = movieRepo.getCredits(id)
                doe.data = res.data
                doe.e = res.e
                if(doe.data != null){
                    doe.loading = false
                }
            }catch (e:Exception){
                doe.e = e
                Log.d("SearchViewModel","error: $e")
            }
            doe.loading = false
            movieCredits.value = doe
        }
        Log.d("SearchViewModel","completed")
    }

    fun getMovieKeywords(id: Int){
        Log.d("MovieViewModel","called")
        movieKeywords.value = DataOrException(KeywordsList(),true,null)
        viewModelScope.launch {
            Log.d("SearchViewModel", "saved")
            val doe = DataOrException<KeywordsList?,Boolean?,Exception>(null,true,null)
            try{
                doe.e = null
                movieKeywords.value.loading = true
                doe.loading = true;
                val res = movieRepo.getKeywords(id)
                doe.data = res.data
                doe.e = res.e
                if(doe.data != null){
                    doe.loading = false
                }
            }catch (e:Exception){
                doe.e = e
                Log.d("SearchViewModel","error: $e")
            }
            doe.loading = false
            movieKeywords.value = doe
        }
        Log.d("SearchViewModel","completed")
    }
    fun getMovieRecommendations(id: Int){
        Log.d("MovieViewModel","called")
        movieRecommendations.value = DataOrException(MoviesList(),true,null)
        viewModelScope.launch {
            Log.d("SearchViewModel", "saved")
            val doe = DataOrException<MoviesList?,Boolean?,Exception>(null,true,null)
            try{
                doe.e = null
                movieRecommendations.value.loading = true
                doe.loading = true
                val res = movieRepo.getRecommendations(id)
                doe.data = res.data
                doe.e = res.e
                if(doe.data != null){
                    doe.loading = false
                }
            }catch (e:Exception){
                doe.e = e
                Log.d("SearchViewModel","error: $e")
            }
            doe.loading = false
            movieRecommendations.value = doe
        }
        Log.d("SearchViewModel","completed")
    }

    //category wise
    fun getPopularMovies(category: String = "popular"){
        Log.d("MovieViewModel","called")
        popularMovies.value = DataOrException(MoviesList(),true,null)
        viewModelScope.launch {
            Log.d("SearchViewModel", "saved")
            val doe = DataOrException<MoviesList?,Boolean?,Exception>(null,true,null)
            try{
                doe.e = null
                popularMovies.value.loading = true
                doe.loading = true;
                val res = movieRepo.getMoviesByCategory(category)
                doe.data = res.data
                doe.e = res.e
                if(doe.data != null){
                    doe.loading = false
                }
            }catch (e:Exception){
                doe.e = e
                Log.d("SearchViewModel","error: $e")
            }
            doe.loading = false
            popularMovies.value = doe
        }
        Log.d("SearchViewModel","completed")
    }

    fun getTopRatedMovies(category: String = "top_rated"){
        Log.d("MovieViewModel","called")
        topRatedMovies.value = DataOrException(MoviesList(),true,null)
        viewModelScope.launch {
            Log.d("SearchViewModel", "saved")
            val doe = DataOrException<MoviesList?,Boolean?,Exception>(null,true,null)
            try{
                doe.e = null
                topRatedMovies.value.loading = true
                doe.loading = true;
                val res = movieRepo.getMoviesByCategory(category)
                doe.data = res.data
                doe.e = res.e
                if(doe.data != null){
                    doe.loading = false
                }
            }catch (e:Exception){
                doe.e = e
                Log.d("SearchViewModel","error: $e")
            }
            doe.loading = false
            topRatedMovies.value = doe
        }
        Log.d("SearchViewModel","completed")
    }

    fun getNowPlayingMovies(category: String = "now_playing"){
        Log.d("MovieViewModel","called")
        nowPlayingMovies.value = DataOrException(MoviesList(),true,null)
        viewModelScope.launch {
            Log.d("SearchViewModel", "saved")
            val doe = DataOrException<MoviesList?,Boolean?,Exception>(null,true,null)
            try{
                doe.e = null
                nowPlayingMovies.value.loading = true
                doe.loading = true;
                val res = movieRepo.getMoviesByCategory(category)
                doe.data = res.data
                doe.e = res.e
                if(doe.data != null){
                    doe.loading = false
                }
            }catch (e:Exception){
                doe.e = e
                Log.d("SearchViewModel","error: $e")
            }
            nowPlayingMovies.value = doe
        }
        Log.d("SearchViewModel","completed")
    }

    fun getUpcomingMovies(category: String = "upcoming"){
        Log.d("MovieViewModel","called")
        upcomingMovies.value = DataOrException(MoviesList(),true,null)
        viewModelScope.launch {
            Log.d("SearchViewModel", "saved")
            val doe = DataOrException<MoviesList?,Boolean?,Exception>(null,true,null)
            try{
                doe.e = null
                upcomingMovies.value.loading = true
                doe.loading = true;
                val res = movieRepo.getMoviesByCategory(category)
                doe.data = res.data
                doe.e = res.e
                if(doe.data != null){
                    doe.loading = false
                }
            }catch (e:Exception){
                doe.e = e
                Log.d("SearchViewModel","error: $e")
            }
            doe.loading = false
            upcomingMovies.value = doe
        }
        Log.d("SearchViewModel","completed")
    }
}