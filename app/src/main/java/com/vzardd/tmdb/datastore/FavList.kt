package com.vzardd.tmdb.datastore

data class FavList(
    var moviesCache: List<MovieCache>? = emptyList()
)
