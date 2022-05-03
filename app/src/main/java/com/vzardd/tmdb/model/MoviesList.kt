package com.vzardd.tmdb.model

data class MoviesList(
    val page: Int? = 0,
    var results: List<Result>? = null,
    val total_pages: Int? = 0,
    val total_results: Int? = 0
)