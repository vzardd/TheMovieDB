package com.vzardd.tmdb.network

import com.vzardd.tmdb.model.Credits
import com.vzardd.tmdb.model.FullMovieDetails
import com.vzardd.tmdb.model.KeywordsList
import com.vzardd.tmdb.model.MoviesList
import com.vzardd.tmdb.util.Constants
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface ApiService {

    @GET("search/movie")
    suspend fun getSearchMoviesList(
        @Query("query")
        query: String,
        @Query("api_key")
        api_key: String = Constants.API_KEY
    ) : MoviesList

    @GET("movie/{id}")
    suspend fun getMovieDetails(
        @Path("id")
        id: Int,
        @Query("api_key")
        api_key: String = Constants.API_KEY
    ) : FullMovieDetails

    @GET("movie/{id}/credits")
    suspend fun getMovieCredits(
        @Path("id")
        id: Int,
        @Query("api_key")
        api_key: String = Constants.API_KEY
    ) : Credits

    @GET("movie/{id}/keywords")
    suspend fun getMovieKeywords(
        @Path("id")
        id: Int,
        @Query("api_key")
        api_key: String = Constants.API_KEY
    ) : KeywordsList

    @GET("movie/{id}/recommendations")
    suspend fun getMovieRecommendations(
        @Path("id")
        id: Int,
        @Query("api_key")
        api_key: String = Constants.API_KEY
    ) : MoviesList

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key")
        api_key: String = Constants.API_KEY
    ) : MoviesList

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key")
        api_key: String = Constants.API_KEY
    ) : MoviesList

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key")
        api_key: String = Constants.API_KEY
    ) : MoviesList

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key")
        api_key: String = Constants.API_KEY
    ) : MoviesList


}