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
import com.vzardd.tmdb.room.FullMovieCache
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

    private val _movieDetails = MutableStateFlow(FullMovieDetails())
    val movieDetails = _movieDetails.asStateFlow()

    private val _movieCredits = MutableStateFlow(Credits())
    val movieCredits = _movieCredits.asStateFlow()

    private val _movieKeywords = MutableStateFlow(KeywordsList())
    val movieKeywords = _movieKeywords.asStateFlow()

    private val _movieRecommendations = MutableStateFlow(MoviesList())
    val movieRecommendations = _movieRecommendations.asStateFlow()
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

    fun reset(){
        _movieDetails.value = FullMovieDetails()
        _movieCredits.value = Credits()
        _movieRecommendations.value = MoviesList()
        _movieKeywords.value = KeywordsList()
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

    fun storeFullMovieDetailsToRoom(id: Int){
        viewModelScope.launch {
            movieRepo.clearMovieCacheForId(id)
            Log.e("MovieViewModel","inside storing to room")

            val res1 = movieRepo.getMovieDetails(id)
            val movieDetails = TMDBUtil.movieObjectToJson(res1.data?: FullMovieDetails())

            val res2 = movieRepo.getCredits(id)
            val movieCredits = TMDBUtil.creditsObjectToJson(res2.data?: Credits())

            val res3 = movieRepo.getKeywords(id)
            val keywordsList = TMDBUtil.keywordsListObjectToJson(res3.data?:KeywordsList())

            val res4 = movieRepo.getRecommendations(id)
            val recommendationList = TMDBUtil.recommendationsObjectToJson(res4.data?:MoviesList())

            val fullMovieCache = FullMovieCache(id,movieDetails,movieCredits,keywordsList,recommendationList)
            Log.e("MovieViewModel","ending storing to room")
            movieRepo.addFullMovieToRoom(fullMovieCache)
        }
    }

    fun getFullMovieDetailsFromRoom(id: Int){
        getMovieDetails(id)
        getMovieCredits(id)
        getMovieKeywords(id)
        getMovieRecommendations(id)
    }

    private fun getMovieDetails(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            movieRepo.getMovieCacheFromRoom(id).distinctUntilChanged()
                .collect{
                    if(it!=null)
                    _movieDetails.value = TMDBUtil.jsonToMovieObject(it)
                }
        }
    }

    private fun getMovieCredits(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            movieRepo.getCreditsCacheFromRoom(id).distinctUntilChanged()
                .collect{
                    if(it!=null)
                    _movieCredits.value = TMDBUtil.jsonToCreditsObject(it)
                }
        }
    }

    private fun getMovieKeywords(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            movieRepo.getKeywordsCacheFromRoom(id).distinctUntilChanged()
                .collect{
                    if(it!=null)
                    _movieKeywords.value = TMDBUtil.jsonToKeywordsListObject(it)
                }
        }
    }
    private fun getMovieRecommendations(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            movieRepo.getRecommendationsCacheFromRoom(id).distinctUntilChanged()
                .collect{
                    if(it!=null)
                    _movieRecommendations.value = TMDBUtil.jsonToRecommendationsObject(it)
                }
        }
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

    suspend fun getFavouritesByIds(idList: List<Int>) : List<MovieDetails>{
        val list = mutableListOf<MovieDetails>()
            for(id in idList){
                list.add(movieRepo.getMovieById(id))
            }
        return list.toList()
    }
}