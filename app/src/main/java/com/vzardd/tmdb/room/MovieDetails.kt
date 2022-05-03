package com.vzardd.tmdb.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_details")
data class MovieDetails(
    val backdrop_path: String = "",
    @PrimaryKey
    val id: Int = 0,
    val overview: String = "",
    val popularity: Double = 0.0,
    val poster_path: String = "",
    val release_date: String = "",
    val title: String = "",
    val vote_average: Double = 0.0,
    var popular: Boolean = false,
    var now_playing: Boolean = false,
    var upcoming: Boolean = false,
    var top_rated: Boolean = false
)
