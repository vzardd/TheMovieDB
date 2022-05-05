package com.vzardd.tmdb.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_cache")
data class FullMovieCache(
    @PrimaryKey
    val id: Int? = 0,
    val movieDetailsCache: String? = "",
    val creditsCache: String? = "",
    val keywordsCache: String? = "",
    val recommendationsCache: String? = ""
)