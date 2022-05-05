package com.vzardd.tmdb.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * from movie_details where popular = 1")
    fun getPopularMoviesFromRoom() : Flow<List<MovieDetails>>

    @Query("SELECT * from movie_details where top_rated = 1")
    fun getTopRatedMoviesFromRoom() : Flow<List<MovieDetails>>

    @Query("SELECT * from movie_details where now_playing = 1")
    fun getNowPlayingMoviesFromRoom() : Flow<List<MovieDetails>>

    @Query("SELECT * from movie_details where upcoming = 1")
    fun getUpcomingMoviesFromRoom() : Flow<List<MovieDetails>>

    @Query( "DELETE from movie_details")
    suspend fun clearMovieDB()

    @Query("SELECT * from movie_details where id =:id")
    suspend fun getMovieById(id: Int): MovieDetails

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMovie(movieDetails: MovieDetails) : Long

    @Query("UPDATE movie_details SET popular = 1 WHERE id = :id")
    suspend fun updatePopular(id: Int)

    @Query("UPDATE movie_details SET top_rated = 1 WHERE id = :id")
    suspend fun updateTopRated(id: Int)

    @Query("UPDATE movie_details SET now_playing = 1 WHERE id = :id")
    suspend fun updateNowPlaying(id: Int)

    @Query("UPDATE movie_details SET upcoming = 1 WHERE id = :id")
    suspend fun updateUpcoming(id: Int)
}