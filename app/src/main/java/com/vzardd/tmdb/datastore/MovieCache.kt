package com.vzardd.tmdb.datastore

data class MovieCache(
    val id: Int = -1,
    val overview: String = "",
    val poster_path: String = "",
    val release_date: String = "",
    val title: String = "",
)
