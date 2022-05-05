package com.vzardd.tmdb.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vzardd.tmdb.model.*
import com.vzardd.tmdb.repository.MovieRepo
import com.vzardd.tmdb.room.MovieDetails
import com.vzardd.tmdb.util.DataOrException
import com.vzardd.tmdb.util.TMDBUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
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

    private val _popularMovies = MutableStateFlow<List<MovieDetails>>(emptyList())
    val popularMovies  = _popularMovies.asStateFlow()

    private val _topRatedMovies = MutableStateFlow<List<MovieDetails>>(emptyList())
    val topRatedMovies = _topRatedMovies.asStateFlow()

    private val _nowPlayingMovies = MutableStateFlow<List<MovieDetails>>(emptyList())
    val nowPlayingMovies = _nowPlayingMovies.asStateFlow()

    private val _upcomingMovies = MutableStateFlow<List<MovieDetails>>(emptyList())
    val upcomingMovies = _upcomingMovies.asStateFlow()


    init {

        if(TMDBUtil.isNetworkAvailable(movieRepo.getContext())){
            Log.d("MVM", "Network available")
            viewModelScope.launch {
                movieRepo.clearMovieDB()
                storePopularMovies()
                storeTopRatedMovies()
                storeNowPlayingMovies()
                storeUpcomingMovies()
            }
        }

        getPopularMoviesFromRoom()
        getTopRatedMoviesFromRoom()
        getNowPlayingMoviesFromRoom()
        getUpcomingMoviesFromRoom()

    }

    fun getPopularMoviesFromRoom(){
        viewModelScope.launch(Dispatchers.IO) {
            movieRepo.getPopularMoviesFromRoom().distinctUntilChanged()
                .collect{
                    if(!it.isNullOrEmpty()){
                        _popularMovies.value = it
                    }
                }
        }
    }

    fun getTopRatedMoviesFromRoom(){
        viewModelScope.launch(Dispatchers.IO) {
            movieRepo.getTopRatedMoviesFromRoom().distinctUntilChanged()
                .collect{
                    if(!it.isNullOrEmpty()){
                        _topRatedMovies.value = it
                    }
                }
        }
    }

    fun getNowPlayingMoviesFromRoom(){
        viewModelScope.launch(Dispatchers.IO) {
            movieRepo.getNowPlayingMoviesFromRoom().distinctUntilChanged()
                .collect{
                    if(!it.isNullOrEmpty()){
                        _nowPlayingMovies.value = it
                    }
                }
        }
    }

    fun getUpcomingMoviesFromRoom(){
        viewModelScope.launch(Dispatchers.IO) {
            movieRepo.getUpcomingMoviesFromRoom().distinctUntilChanged()
                .collect{
                    if(!it.isNullOrEmpty()){
                        _upcomingMovies.value = it
                    }
                }
        }
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
    fun storePopularMovies(){
        viewModelScope.launch {
            Log.d("Store Popular", "called")
            val res = movieRepo.getPopularMovies()
            for(movie in res.data?.results?: emptyList()){
                Log.d("Store Popular", "inside loop")
                val status = movieRepo.addMovieToRoom(
                    MovieDetails(
                        movie.backdrop_path?:"",
                        movie.id?:0,
                        movie.overview?:"Info Unavailable.",
                        movie.popularity?:0.0,
                        movie.poster_path?:"",
                        movie.release_date?:" ",
                        movie.title?:"Untitled",
                        movie.vote_average?:0.0,
                        true
                    )
                )

                if(status == -1L){
                    movieRepo.updatePopular(movie.id)
                }
                Log.d("Store Popular", "over")
            }
        }
    }

    fun storeTopRatedMovies(){
        viewModelScope.launch {
            Log.d("Store Top Rated", "called")
            val res = movieRepo.getTopRatedMovies()
            for(movie in res.data?.results?: emptyList()){
                Log.d("Store Top Rated", "inside loop")
                val status = movieRepo.addMovieToRoom(
                    MovieDetails(
                        movie.backdrop_path?:"",
                        movie.id?:0,
                        movie.overview?:"Info Unavailable.",
                        movie.popularity?:0.0,
                        movie.poster_path?:"",
                        movie.release_date?:" ",
                        movie.title?:"Untitled",
                        movie.vote_average?:0.0,
                        top_rated = true
                    )
                )

                if(status == -1L){
                    movieRepo.updateTopRated(movie.id)
                }
                Log.d("Store Top Rated", "over")
            }
        }
    }

    fun storeNowPlayingMovies(){
        viewModelScope.launch {
            Log.d("Store Now Playing", "called")
            val res = movieRepo.getNowPlayingMovies()
            for(movie in res.data?.results?: emptyList()){
                Log.d("Store Now Playing", "inside loop")
                val status = movieRepo.addMovieToRoom(
                    MovieDetails(
                        movie.backdrop_path?:"",
                        movie.id?:0,
                        movie.overview?:"Info Unavailable.",
                        movie.popularity?:0.0,
                        movie.poster_path?:"",
                        movie.release_date?:" ",
                        movie.title?:"Untitled",
                        movie.vote_average?:0.0,
                        now_playing = true
                    )
                )

                if(status == -1L){
                    movieRepo.updateNowPlaying(movie.id)
                }
                Log.d("Store Now Playing", "over")
            }
        }
    }

    fun storeUpcomingMovies(){
        viewModelScope.launch {
            Log.d("Store Upcoming", "called")
            val res = movieRepo.getUpcomingMovies()
            for(movie in res.data?.results?: emptyList()){
                Log.d("Store Upcoming", "Inside Loop")
                val status = movieRepo.addMovieToRoom(
                    MovieDetails(
                        movie.backdrop_path?:"",
                        movie.id?:0,
                        movie.overview?:"Info Unavailable.",
                        movie.popularity?:0.0,
                        movie.poster_path?:"",
                        movie.release_date?:" ",
                        movie.title?:"Untitled",
                        movie.vote_average?:0.0,
                        upcoming = true
                    )
                )

                if(status == -1L){
                    movieRepo.updateUpcoming(movie.id)
                }
            }
            Log.d("Store Upcoming", "over")
        }
    }

    fun getFavouritesByIds(idList: List<Int>) : List<MovieDetails>{
        val list = mutableListOf<MovieDetails>()
        viewModelScope.launch {
            for(id in idList){
                list.add(movieRepo.getMovieById(id))
            }
        }
        return list.toList()
    }
}