package com.vzardd.tmdb.model

data class Result(
    val backdrop_path: String = "",
    val id: Int = -1,
    val overview: String = "",
    val popularity: Double = 0.0,
    val poster_path: String = "",
    val release_date: String = "",
    val title: String = "",
    val vote_average: Double = 0.0,
){
    companion object{
        operator fun invoke(
            backdrop_path: String? = null,
            id: Int? = null,
            overview: String? = null,
            popularity: Double? = null,
            poster_path: String? = null,
            release_date: String? = null,
            title: String? = null,
            vote_average: Double? = null,
        ) = Result(
            backdrop_path ?: "",
            id ?: 0,
            overview ?: "Info Unavailable",
            popularity ?: 0.0,
            poster_path ?: "",
            release_date ?: " ",
            title ?: "Untitled",
            vote_average ?: 0.0,
        )
    }
}