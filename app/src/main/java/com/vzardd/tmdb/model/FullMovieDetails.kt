package com.vzardd.tmdb.model

data class FullMovieDetails(
    val adult: Boolean? = false,
    val backdrop_path: String? = "",
    val budget: Int? = 0,
    val genres: List<Genre>? = null,
    val id: Int? = 0,
    val original_language: String? = "",
    val overview: String? = "Info Unavailable",
    val popularity: Double? = 0.0,
    val poster_path: String? = "",
    val release_date: String? = " ",
    val spoken_languages: List<SpokenLanguage>? = null,
    val revenue: Int? = 0,
    val runtime: Int? = 0, //duration
    val status: String? = "Not Released",
    val tagline: String? = " ",
    val title: String? = "Untitled",
    val video: Boolean? = false,
    val vote_average: Double? = 0.0,
    val vote_count: Int? = 0,
){
    companion object{
        operator fun invoke(
            adult: Boolean? = null,
            backdrop_path: String? = null,
            budget: Int? = null,
            genres: List<Genre>? = null,
            id: Int? = null,
            original_language: String? = null,
            overview: String? = null,
            popularity: Double? = null,
            poster_path: String? = null,
            release_date: String? = null,
            spoken_languages: List<SpokenLanguage>? = null,
            revenue: Int? = null,
            runtime: Int? = null, //duration
            status: String? = null,
            tagline: String? = null,
            title: String? = null,
            video: Boolean? = null,
            vote_average: Double? = null,
            vote_count: Int? = null

        ) = FullMovieDetails(
            adult ?: false,
            backdrop_path ?: "",
            budget ?: 0,
            genres ?: emptyList(),
            id ?: 0,
            original_language ?: "",
            overview ?: "Info Unavailable",
            popularity ?: 0.0,
            poster_path ?: "",
            release_date ?: " ",
            spoken_languages ?: emptyList(),
            revenue ?: 0,
            runtime ?: 0, //duration
            status ?: "Not Released",
            tagline ?: " ",
            title ?: "Untitled",
            video ?: false,
            vote_average ?: 0.0,
            vote_count ?: 0,

            )
    }
}